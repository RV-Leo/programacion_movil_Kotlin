package com.tecsup.cafecampus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.admin.PanelScreen
import com.tecsup.cafecampus.ui.admin.PedidosAdminScreen
import com.tecsup.cafecampus.ui.cliente.ConfirmarPedidoScreen
import com.tecsup.cafecampus.ui.cliente.DetalleProductoScreen
import com.tecsup.cafecampus.ui.cliente.LoginScreen
import com.tecsup.cafecampus.ui.cliente.MenuScreen
import com.tecsup.cafecampus.ui.cliente.MisPedidosScreen
import com.tecsup.cafecampus.ui.cliente.SeguimientoScreen
import com.tecsup.cafecampus.ui.cliente.SplashScreen
import com.tecsup.cafecampus.viewmodel.CafeViewModelFactory
import com.tecsup.cafecampus.viewmodel.MenuViewModel
import com.tecsup.cafecampus.viewmodel.PanelAdminViewModel
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel
import com.tecsup.cafecampus.viewmodel.PedidosAdminViewModel
import com.tecsup.cafecampus.viewmodel.SesionViewModel

@Composable
fun NavCafe(
    navController: NavHostController,
    factory: CafeViewModelFactory
) {
    val sesionViewModel: SesionViewModel = viewModel(factory = factory)
    val menuViewModel: MenuViewModel = viewModel(factory = factory)
    val pedidosClienteViewModel: PedidosClienteViewModel = viewModel(factory = factory)
    val panelAdminViewModel: PanelAdminViewModel = viewModel(factory = factory)
    val pedidosAdminViewModel: PedidosAdminViewModel = viewModel(factory = factory)

    NavHost(navController = navController, startDestination = Rutas.Splash.ruta) {
        composable(Rutas.Splash.ruta) {
            SplashScreen(vm = sesionViewModel) { destino ->
                navController.navigate(destino) {
                    popUpTo(Rutas.Splash.ruta) { inclusive = true }
                }
            }
        }

        composable(Rutas.Login.ruta) {
            LoginScreen(vm = sesionViewModel) { rol ->
                val destino = if (rol.equals(Rol.ADMIN.name, ignoreCase = true)) Rutas.Panel.ruta else Rutas.Menu.ruta
                sesionViewModel.usuarioActual.value?.id?.let(pedidosClienteViewModel::establecerCliente)
                navController.navigate(destino) {
                    popUpTo(Rutas.Login.ruta) { inclusive = true }
                }
            }
        }

        composable(Rutas.Menu.ruta) {
            MenuScreen(
                vm = menuViewModel,
                onProducto = { id -> navController.navigate(Rutas.DetalleProducto.crear(id)) },
                onAbrirCarrito = { navController.navigate(Rutas.ConfirmarPedido.ruta) },
                onMisPedidos = { navController.navigate(Rutas.MisPedidos.ruta) },
                onCerrarSesion = {
                    sesionViewModel.cerrarSesion()
                    menuViewModel.vaciar()
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(Rutas.Menu.ruta) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Rutas.DetalleProducto.ruta,
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: return@composable
            DetalleProductoScreen(
                vm = menuViewModel,
                productoId = productoId,
                onVolver = { navController.popBackStack() },
                onAgregado = { navController.popBackStack() }
            )
        }

        composable(Rutas.ConfirmarPedido.ruta) {
            ConfirmarPedidoScreen(
                pedidosVm = pedidosClienteViewModel,
                menuVm = menuViewModel,
                onVolver = { navController.popBackStack() },
                onPedidoConfirmado = { id -> navController.navigate(Rutas.Seguimiento.crear(id.toInt())) }
            )
        }

        composable(
            route = Rutas.Seguimiento.ruta,
            arguments = listOf(navArgument("pedidoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val pedidoId = backStackEntry.arguments?.getInt("pedidoId") ?: return@composable
            SeguimientoScreen(
                vm = pedidosClienteViewModel,
                pedidoId = pedidoId,
                onVolver = { navController.popBackStack() },
                onMisPedidos = {
                    navController.navigate(Rutas.MisPedidos.ruta) {
                        popUpTo(Rutas.Seguimiento.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.MisPedidos.ruta) {
            MisPedidosScreen(
                vm = pedidosClienteViewModel,
                onVolver = { navController.popBackStack() },
                onAbrirPedido = { id -> navController.navigate(Rutas.Seguimiento.crear(id)) }
            )
        }

        composable(Rutas.Panel.ruta) {
            PanelScreen(
                vm = panelAdminViewModel,
                pedidosVm = pedidosAdminViewModel,
                onVerPedidos = { navController.navigate(Rutas.PedidosAdmin.ruta) },
                onCerrarSesion = {
                    sesionViewModel.cerrarSesion()
                    pedidosClienteViewModel.establecerCliente(0)
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(Rutas.Panel.ruta) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Rutas.PedidosAdmin.ruta) {
            PedidosAdminScreen(
                vm = pedidosAdminViewModel,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
