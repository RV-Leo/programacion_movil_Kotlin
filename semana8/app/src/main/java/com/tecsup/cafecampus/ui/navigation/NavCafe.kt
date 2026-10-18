package com.tecsup.cafecampus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.admin.AjustesScreen
import com.tecsup.cafecampus.ui.admin.DetallePedidoAdminScreen
import com.tecsup.cafecampus.ui.admin.FormularioProductoScreen
import com.tecsup.cafecampus.ui.admin.MenuAdminScreen
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
import com.tecsup.cafecampus.viewmodel.MenuAdminViewModel
import com.tecsup.cafecampus.viewmodel.MenuViewModel
import com.tecsup.cafecampus.viewmodel.PanelAdminViewModel
import com.tecsup.cafecampus.viewmodel.PedidosAdminViewModel
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel
import com.tecsup.cafecampus.viewmodel.SesionViewModel

@Composable
fun NavCafe(navController: NavHostController, factory: CafeViewModelFactory) {
    val sesionViewModel: SesionViewModel = viewModel(factory = factory)
    val menuViewModel: MenuViewModel = viewModel(factory = factory)
    val pedidosClienteViewModel: PedidosClienteViewModel = viewModel(factory = factory)
    val panelAdminViewModel: PanelAdminViewModel = viewModel(factory = factory)
    val pedidosAdminViewModel: PedidosAdminViewModel = viewModel(factory = factory)
    val menuAdminViewModel: MenuAdminViewModel = viewModel(factory = factory)
    val recibirPedidos by sesionViewModel.recibirPedidos.collectAsState()

    NavHost(navController = navController, startDestination = Rutas.Splash.ruta) {
        composable(Rutas.Splash.ruta) {
            SplashScreen(vm = sesionViewModel) { destino ->
                sesionViewModel.usuarioActual.value?.id?.let(pedidosClienteViewModel::establecerCliente)
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
                recibirPedidos = recibirPedidos,
                onProducto = { id -> navController.navigate(Rutas.DetalleProducto.crear(id)) },
                onAbrirCarrito = { navController.navigate(Rutas.ConfirmarPedido.ruta) },
                onMisPedidos = { navController.navigate(Rutas.MisPedidos.ruta) },
                onAjustes = { navController.navigate(Rutas.Ajustes.ruta) },
                onCerrarSesion = {
                    sesionViewModel.cerrarSesion()
                    menuViewModel.vaciar()
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            Rutas.DetalleProducto.ruta,
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("productoId") ?: return@composable
            DetalleProductoScreen(menuViewModel, id, onVolver = { navController.popBackStack() }, onAgregado = { navController.popBackStack() })
        }
        composable(Rutas.ConfirmarPedido.ruta) {
            ConfirmarPedidoScreen(
                pedidosVm = pedidosClienteViewModel,
                menuVm = menuViewModel,
                recibirPedidos = recibirPedidos,
                onVolver = { navController.popBackStack() },
                onPedidoConfirmado = { id -> navController.navigate(Rutas.Seguimiento.crear(id.toInt())) }
            )
        }
        composable(
            Rutas.Seguimiento.ruta,
            arguments = listOf(navArgument("pedidoId") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("pedidoId") ?: return@composable
            SeguimientoScreen(
                pedidosClienteViewModel,
                id,
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
                pedidosClienteViewModel,
                onVolver = { navController.popBackStack() },
                onAbrirPedido = { id -> navController.navigate(Rutas.Seguimiento.crear(id)) }
            )
        }
        composable(Rutas.Panel.ruta) {
            PanelScreen(
                vm = panelAdminViewModel,
                pedidosVm = pedidosAdminViewModel,
                onVerPedidos = { navController.navigate(Rutas.PedidosAdmin.ruta) },
                onVerMenu = { navController.navigate(Rutas.MenuAdmin.ruta) },
                onAjustes = { navController.navigate(Rutas.Ajustes.ruta) },
                onCerrarSesion = {
                    sesionViewModel.cerrarSesion()
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Rutas.Ajustes.ruta) {
            AjustesScreen(
                vm = sesionViewModel,
                onVolver = { navController.popBackStack() },
                onCambiarPerfil = { rol ->
                    val destino = if (rol.equals(Rol.ADMIN.name, ignoreCase = true)) Rutas.Panel.ruta else Rutas.Menu.ruta
                    sesionViewModel.usuarioActual.value?.id?.let(pedidosClienteViewModel::establecerCliente)
                    navController.navigate(destino) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onCerrarSesion = {
                    sesionViewModel.cerrarSesion()
                    menuViewModel.vaciar()
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Rutas.PedidosAdmin.ruta) {
            PedidosAdminScreen(
                vm = pedidosAdminViewModel,
                onVolver = { navController.popBackStack() },
                onAbrirDetalle = { id -> navController.navigate(Rutas.DetallePedidoAdmin.crear(id)) }
            )
        }
        composable(
            Rutas.DetallePedidoAdmin.ruta,
            arguments = listOf(navArgument("pedidoId") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("pedidoId") ?: return@composable
            DetallePedidoAdminScreen(pedidosAdminViewModel, id, onVolver = { navController.popBackStack() })
        }
        composable(Rutas.MenuAdmin.ruta) {
            MenuAdminScreen(
                vm = menuAdminViewModel,
                onVolver = { navController.popBackStack() },
                onNuevo = { navController.navigate(Rutas.FormularioProducto.nuevo()) },
                onEditar = { id -> navController.navigate(Rutas.FormularioProducto.editar(id)) }
            )
        }
        composable(
            Rutas.FormularioProducto.ruta,
            arguments = listOf(navArgument("productoId") { type = NavType.IntType; defaultValue = -1 })
        ) { entry ->
            val id = entry.arguments?.getInt("productoId")?.takeIf { it > 0 }
            FormularioProductoScreen(menuAdminViewModel, productoId = id, onVolver = { navController.popBackStack() })
        }
    }
}
