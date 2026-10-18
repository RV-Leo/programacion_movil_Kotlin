package com.tecsup.cafecampus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.admin.PanelScreen
import com.tecsup.cafecampus.ui.cliente.LoginScreen
import com.tecsup.cafecampus.ui.cliente.MenuScreen
import com.tecsup.cafecampus.ui.cliente.SplashScreen
import com.tecsup.cafecampus.viewmodel.CafeViewModelFactory
import com.tecsup.cafecampus.viewmodel.MenuViewModel
import com.tecsup.cafecampus.viewmodel.PanelAdminViewModel
import com.tecsup.cafecampus.viewmodel.SesionViewModel

@Composable
fun NavCafe(
    navController: NavHostController,
    factory: CafeViewModelFactory
) {
    val sesionViewModel: SesionViewModel = viewModel(factory = factory)
    val menuViewModel: MenuViewModel = viewModel(factory = factory)
    val panelAdminViewModel: PanelAdminViewModel = viewModel(factory = factory)

    NavHost(
        navController = navController,
        startDestination = Rutas.Splash.ruta
    ) {
        composable(Rutas.Splash.ruta) {
            SplashScreen(
                vm = sesionViewModel,
                onNavigate = { destino ->
                    navController.navigate(destino) {
                        popUpTo(Rutas.Splash.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.Login.ruta) {
            LoginScreen(
                vm = sesionViewModel,
                onLoginSuccess = { rol ->
                    val destino = if (rol.equals(Rol.ADMIN.name, ignoreCase = true)) {
                        Rutas.Panel.ruta
                    } else {
                        Rutas.Menu.ruta
                    }
                    navController.navigate(destino) {
                        popUpTo(Rutas.Login.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.Menu.ruta) {
            MenuScreen(
                vm = menuViewModel,
                onProducto = { _ ->
                }
            )
        }

        composable(Rutas.Panel.ruta) {
            PanelScreen(
                vm = panelAdminViewModel
            )
        }
    }
}
