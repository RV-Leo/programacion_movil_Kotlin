package com.tecsup.cafecampus.ui.navigation

sealed class Rutas(val ruta: String) {
    object Splash : Rutas("splash")
    object Login : Rutas("login")
    object Menu : Rutas("menu")
    object DetalleProducto : Rutas("detalle/{productoId}") {
        fun crear(productoId: Int) = "detalle/$productoId"
    }
    object ConfirmarPedido : Rutas("confirmar-pedido")
    object Seguimiento : Rutas("seguimiento/{pedidoId}") {
        fun crear(pedidoId: Int) = "seguimiento/$pedidoId"
    }
    object MisPedidos : Rutas("mis-pedidos")
    object Panel : Rutas("panel")
}
