package com.tecsup.computerstore

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.computerstore.domain.model.Rol
import com.tecsup.computerstore.domain.model.Sesion
import com.tecsup.computerstore.ui.admin.AdminScreen
import com.tecsup.computerstore.ui.admin.AdminViewModel
import com.tecsup.computerstore.ui.admin.AdminViewModelFactory
import com.tecsup.computerstore.ui.detalle.DetalleScreen
import com.tecsup.computerstore.ui.detalle.DetalleViewModel
import com.tecsup.computerstore.ui.favoritos.FavoritosScreen
import com.tecsup.computerstore.ui.favoritos.FavoritosViewModel
import com.tecsup.computerstore.ui.lista.ListaScreen
import com.tecsup.computerstore.ui.lista.ListaViewModel
import com.tecsup.computerstore.ui.login.LoginScreen
import com.tecsup.computerstore.ui.login.LoginViewModel
import com.tecsup.computerstore.ui.theme.ComputerStoreTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val appContainer by lazy { DefaultAppContainer(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComputerStoreTheme(dynamicColor = false) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    ComputerStoreApp(appContainer)
                }
            }
        }
    }
}

@Composable
private fun ComputerStoreApp(appContainer: AppContainer) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val notificationPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { }
    var session by remember { mutableStateOf<Sesion?>(null) }
    var sessionChecked by remember { mutableStateOf(false) }

    LaunchedEffect(appContainer) {
        session = runCatching { appContainer.authRepository.getSesionActual().first() }.getOrNull()
        sessionChecked = true
    }
    LaunchedEffect(session?.uid) {
        if (session != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    if (!sessionChecked) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }

    val startDestination = if (session == null) "login" else "catalogo"
    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            val vm: LoginViewModel = viewModel(factory = LoginViewModel.Factory(appContainer))
            LoginScreen(vm) { newSession ->
                session = newSession
                navController.navigate("catalogo") { popUpTo("login") { inclusive = true } }
            }
        }
        composable("catalogo") {
            val vm: ListaViewModel = viewModel(key = "lista-${session?.uid}", factory = ListaViewModel.Factory(appContainer))
            ListaScreen(
                viewModel = vm,
                usuario = session?.usuario ?: "Leo",
                esAdmin = session?.rol == Rol.ADMIN,
                onItemClick = { navController.navigate("detalle/$it") },
                onFavoritosClick = { navController.navigate("favoritos") },
                onAdminClick = { if (session?.rol == Rol.ADMIN) navController.navigate("admin") },
                onLogout = {
                    scope.launch {
                        appContainer.authRepository.logout()
                        session = null
                        navController.navigate("login") { popUpTo("catalogo") { inclusive = true } }
                    }
                }
            )
        }
        composable("favoritos") {
            val vm: FavoritosViewModel = viewModel(factory = FavoritosViewModel.Factory(appContainer))
            FavoritosScreen(vm, onBack = { navController.popBackStack() }, onItemClick = { navController.navigate("detalle/$it") })
        }
        composable("admin") {
            if (session?.rol == Rol.ADMIN) {
                val vm: AdminViewModel = viewModel(factory = AdminViewModelFactory.create(appContainer))
                AdminScreen(vm, onBack = { navController.popBackStack() })
            } else {
                LaunchedEffect(Unit) { navController.popBackStack() }
            }
        }
        composable("detalle/{itemId}", arguments = listOf(navArgument("itemId") { type = NavType.IntType })) { entry ->
            val id = entry.arguments?.getInt("itemId") ?: return@composable
            val vm: DetalleViewModel = viewModel(key = "detalle-$id", factory = DetalleViewModel.Factory(id, appContainer))
            DetalleScreen(viewModel = vm, onVolverClick = { navController.popBackStack() })
        }
    }
}
