package com.tecsup.cafecampus.ui.navigation

sealed class Rutas(val ruta: String) {
    object Splash : Rutas("splash")
    object Login : Rutas("login")
    object Menu : Rutas("menu")
    object DetalleProducto : Rutas("detalle/{productoId}") {
        fun crear(productoId: Int) = "detalle/$productoId"
    }
    object Panel : Rutas("panel")
}
