package com.tecsup.mibodega.ui.cliente

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.cliente.screens.carrito.CarritoScreen
import com.tecsup.mibodega.ui.cliente.screens.detalle.DetalleProductoScreen
import com.tecsup.mibodega.ui.cliente.screens.confirmacion.ConfirmacionScreen
import com.tecsup.mibodega.ui.cliente.screens.entrega.DatosEntregaScreen
import com.tecsup.mibodega.ui.cliente.screens.inicio.InicioScreen
import com.tecsup.mibodega.ui.cliente.screens.login.PantallaLogin
import com.tecsup.mibodega.ui.cliente.screens.perfil.PerfilScreen
import com.tecsup.mibodega.ui.cliente.screens.registro.PantallaCrearCuenta
import com.tecsup.mibodega.ui.theme.VerdeBodega

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route
    val mostrarBarra = rutaActual == Rutas.INICIO || rutaActual == Rutas.CARRITO || rutaActual == Rutas.PERFIL
    var carrito by remember { mutableStateOf<List<ItemCarrito>>(emptyList()) }
    var nombreEntrega by remember { mutableStateOf("") }
    var direccionEntrega by remember { mutableStateOf("") }
    var totalPedido by remember { mutableStateOf(0.0) }

    // Datos del usuario (perfil / registro)
    var nombreUsuario by remember { mutableStateOf("Juan Pérez") }
    var telefonoUsuario by remember { mutableStateOf("987 654 321") }
    var direccionUsuario by remember { mutableStateOf("Av. Los Olivos 123") }
    var referenciaUsuario by remember { mutableStateOf("Frente al parque") }

    Scaffold(
        bottomBar = {
            if (mostrarBarra) {
                NavigationBar {
                    NavigationBarItem(
                        selected = rutaActual == Rutas.INICIO,
                        onClick = {
                            navController.navigate(Rutas.INICIO) {
                                popUpTo(Rutas.INICIO) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                        label = { Text("Inicio") },
                        colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                            selectedIconColor = VerdeBodega,
                            selectedTextColor = VerdeBodega
                        )
                    )
                    NavigationBarItem(
                        selected = rutaActual == Rutas.CARRITO,
                        onClick = {
                            navController.navigate(Rutas.CARRITO) {
                                popUpTo(Rutas.INICIO) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito") },
                        label = { Text("Carrito") },
                        colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                            selectedIconColor = VerdeBodega,
                            selectedTextColor = VerdeBodega
                        )
                    )
                    NavigationBarItem(
                        selected = rutaActual == Rutas.PERFIL,
                        onClick = {
                            navController.navigate(Rutas.PERFIL) {
                                popUpTo(Rutas.INICIO) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                        label = { Text("Perfil") },
                        colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                            selectedIconColor = VerdeBodega,
                            selectedTextColor = VerdeBodega
                        )
                    )
                }
            }
        }
    ) { paddingInterior ->
        NavHost(
            navController = navController,
            startDestination = Rutas.LOGIN,
            modifier = Modifier.padding(paddingInterior)
        ) {
            composable(Rutas.LOGIN) {
                PantallaLogin(
                    onIngresar = {
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.LOGIN) { inclusive = true }
                        }
                    },
                    onCrearCuenta = { navController.navigate(Rutas.REGISTRO) }
                )
            }

            composable(Rutas.REGISTRO) {
                PantallaCrearCuenta(
                    onVolver = { navController.popBackStack() },
                    onCrearCuenta = { nombre, telefono, direccion, referencia ->
                        nombreUsuario = nombre
                        telefonoUsuario = telefono
                        direccionUsuario = direccion
                        referenciaUsuario = referencia
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

            composable(Rutas.INICIO) {
                InicioScreen(
                    cantidadCarrito = carrito.sumOf { it.cantidad },
                    onVerCarrito = { navController.navigate(Rutas.CARRITO) },
                    onProductoClick = { producto ->
                        navController.navigate(Rutas.detalle(producto.id))
                    },
                    onAgregarProducto = { producto ->
                        carrito = agregarOSumarProducto(carrito, producto, 1)
                        navController.navigate(Rutas.CARRITO)
                    }
                )
            }

            composable(
                route = Rutas.DETALLE,
                arguments = listOf(navArgument("productoId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
                val producto = listaProductosFake.first { it.id == productoId }

                DetalleProductoScreen(
                    producto = producto,
                    onVolver = { navController.popBackStack() },
                    onAgregarAlCarrito = { productoSeleccionado, cantidad ->
                        carrito = agregarOSumarProducto(carrito, productoSeleccionado, cantidad)
                        navController.navigate(Rutas.CARRITO)
                    }
                )
            }

            composable(Rutas.CARRITO) {
                CarritoScreen(
                    carrito = carrito,
                    onVolver = { navController.popBackStack() },
                    onIncrementar = { producto ->
                        carrito = carrito.map {
                            if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + 1) else it
                        }
                    },
                    onDecrementar = { producto ->
                        carrito = carrito.mapNotNull {
                            when {
                                it.producto.id != producto.id -> it
                                it.cantidad > 1 -> it.copy(cantidad = it.cantidad - 1)
                                else -> null
                            }
                        }
                    },
                    onEliminar = { producto ->
                        carrito = carrito.filterNot { it.producto.id == producto.id }
                    },
                    onContinuarPedido = { total ->
                        totalPedido = total
                        navController.navigate(Rutas.DATOS_ENTREGA)
                    }
                )
            }

            composable(Rutas.DATOS_ENTREGA) {
                DatosEntregaScreen(
                    totalPedido = totalPedido,
                    onVolver = { navController.popBackStack() },
                    onConfirmar = { nombre, _, direccion, _ ->
                        nombreEntrega = nombre
                        direccionEntrega = direccion
                        carrito = emptyList()
                        navController.navigate(Rutas.CONFIRMACION) {
                            popUpTo(Rutas.INICIO) { inclusive = false }
                        }
                    }
                )
            }

            composable(Rutas.CONFIRMACION) {
                ConfirmacionScreen(
                    nombre = nombreEntrega,
                    direccion = direccionEntrega,
                    totalPedido = totalPedido,
                    onVolverInicio = {
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.INICIO) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Rutas.PERFIL) {
                PerfilScreen(
                    nombre = nombreUsuario,
                    telefono = telefonoUsuario,
                    direccion = direccionUsuario,
                    referencia = referenciaUsuario,
                    onCerrarSesion = {
                        carrito = emptyList()
                        navController.navigate(Rutas.LOGIN) {
                            popUpTo(Rutas.LOGIN) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

private fun agregarOSumarProducto(
    carrito: List<ItemCarrito>,
    producto: Producto,
    cantidad: Int
): List<ItemCarrito> {
    val itemExistente = carrito.find { it.producto.id == producto.id }
    return if (itemExistente != null) {
        carrito.map {
            if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + cantidad) else it
        }
    } else {
        carrito + ItemCarrito(producto = producto, cantidad = cantidad)
    }
}
