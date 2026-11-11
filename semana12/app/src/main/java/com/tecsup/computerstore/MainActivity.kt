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
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.computerstore.domain.model.Rol
import com.tecsup.computerstore.domain.model.Sesion
import com.tecsup.computerstore.ui.admin.AdminScreen
import com.tecsup.computerstore.ui.admin.AdminViewModel
import com.tecsup.computerstore.ui.admin.AdminViewModelFactory
import com.tecsup.computerstore.ui.carrito.CarritoScreen
import com.tecsup.computerstore.ui.carrito.CheckoutScreen
import com.tecsup.computerstore.ui.carrito.LineaCarrito
import com.tecsup.computerstore.ui.detalle.DetalleScreen
import com.tecsup.computerstore.ui.detalle.DetalleViewModel
import com.tecsup.computerstore.ui.favoritos.FavoritosScreen
import com.tecsup.computerstore.ui.favoritos.FavoritosViewModel
import com.tecsup.computerstore.ui.lista.ListaScreen
import com.tecsup.computerstore.ui.lista.ListaViewModel
import com.tecsup.computerstore.ui.login.LoginScreen
import com.tecsup.computerstore.ui.login.LoginViewModel
import com.tecsup.computerstore.ui.perfil.PerfilScreen
import com.tecsup.computerstore.ui.registro.RegistroScreen
import com.tecsup.computerstore.ui.registro.RegistroViewModel
import com.tecsup.computerstore.ui.theme.ComputerStoreTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private data class BottomDestination(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val bottomDestinations = listOf(
    BottomDestination("catalogo", "Inicio", Icons.Default.Home),
    BottomDestination("carrito", "Carrito", Icons.Default.ShoppingCart),
    BottomDestination("perfil", "Perfil", Icons.Default.Person)
)

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
    var cart by remember { mutableStateOf<Map<Int, LineaCarrito>>(emptyMap()) }

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

    val navEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navEntry?.destination?.route
    val startDestination = if (session == null) "login" else "catalogo"
    val showBottomBar = session != null && currentRoute in setOf("catalogo", "carrito", "perfil")

    fun navigateToTab(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    val cerrarSesion: () -> Unit = {
        scope.launch {
            appContainer.authRepository.logout()
            cart = emptyMap()
            session = null
            navController.navigate("login") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    bottomDestinations.forEach { destination ->
                        NavigationBarItem(
                            selected = currentRoute == destination.route,
                            onClick = { navigateToTab(destination.route) },
                            icon = {
                                if (destination.route == "carrito" && cart.isNotEmpty()) {
                                    BadgedBox(badge = { Badge { Text(cart.values.sumOf { it.cantidad }.toString()) } }) {
                                        Icon(destination.icon, contentDescription = destination.label)
                                    }
                                } else {
                                    Icon(destination.icon, contentDescription = destination.label)
                                }
                            },
                            label = { Text(destination.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                val vm: LoginViewModel = viewModel(factory = LoginViewModel.Factory(appContainer))
                LoginScreen(
                    viewModel = vm,
                    onRegistrarseClick = { navController.navigate("registro") },
                    onLoginSuccess = { newSession ->
                        session = newSession
                        navController.navigate("catalogo") { popUpTo("login") { inclusive = true } }
                    }
                )
            }
            composable("registro") {
                val vm: RegistroViewModel = viewModel(factory = RegistroViewModel.Factory(appContainer))
                RegistroScreen(
                    viewModel = vm,
                    onVolverClick = { navController.popBackStack() },
                    onRegistroSuccess = { newSession ->
                        session = newSession
                        navController.navigate("catalogo") { popUpTo("registro") { inclusive = true } }
                    }
                )
            }
            composable("catalogo") {
                val vm: ListaViewModel = viewModel(key = "lista-${session?.uid}", factory = ListaViewModel.Factory(appContainer))
                ListaScreen(
                    viewModel = vm,
                    usuario = session?.usuario ?: "Usuario",
                    esAdmin = session?.rol == Rol.ADMIN,
                    onItemClick = { navController.navigate("detalle/$it") },
                    onFavoritosClick = { navController.navigate("favoritos") },
                    onAdminClick = { if (session?.rol == Rol.ADMIN) navController.navigate("admin") }
                )
            }
            composable("carrito") {
                CarritoScreen(
                    lineas = cart.values.toList(),
                    onBrowseProducts = { navigateToTab("catalogo") },
                    onQuantityChange = { id, cantidad ->
                        val linea = cart[id]
                        if (linea != null) {
                            if (cantidad <= 0) cart = cart - id
                            else cart = cart + (id to linea.copy(cantidad = cantidad.coerceAtMost(linea.item.stock)))
                        }
                    },
                    onRemove = { id -> cart = cart - id },
                    onCheckout = { navController.navigate("checkout") }
                )
            }
            composable("checkout") {
                CheckoutScreen(
                    lineas = cart.values.toList(),
                    correo = session?.email.orEmpty(),
                    onBack = { navController.popBackStack() },
                    onOrderComplete = {
                        cart = emptyMap()
                        navigateToTab("catalogo")
                    }
                )
            }
            composable("perfil") {
                PerfilScreen(
                    nombre = session?.usuario ?: "Usuario",
                    correo = session?.email.orEmpty(),
                    uid = session?.uid.orEmpty(),
                    esAdmin = session?.rol == Rol.ADMIN,
                    onFavoritesClick = { navController.navigate("favoritos") },
                    onAdminClick = { if (session?.rol == Rol.ADMIN) navController.navigate("admin") },
                    onLogout = cerrarSesion
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
                DetalleScreen(
                    viewModel = vm,
                    onVolverClick = { navController.popBackStack() },
                    onAddToCart = { item, cantidad ->
                        val actual = cart[item.id]?.cantidad ?: 0
                        val nuevaCantidad = (actual + cantidad).coerceAtMost(item.stock)
                        cart = cart + (item.id to LineaCarrito(item, nuevaCantidad))
                        navigateToTab("carrito")
                    }
                )
            }
        }
    }
}
