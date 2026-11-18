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
    object Ajustes : Rutas("ajustes")
    object PedidosAdmin : Rutas("pedidos-admin")
    object DetallePedidoAdmin : Rutas("detalle-pedido-admin/{pedidoId}") {
        fun crear(pedidoId: Int) = "detalle-pedido-admin/$pedidoId"
    }
    object MenuAdmin : Rutas("menu-admin")
    object FormularioProducto : Rutas("form-producto?productoId={productoId}") {
        fun nuevo() = "form-producto"
        fun editar(productoId: Int) = "form-producto?productoId=$productoId"
    }
}
