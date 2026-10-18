package com.tecsup.cafecampus.ui.navigation

sealed class Rutas(val ruta: String) {
    object Splash : Rutas("splash")
    object Login : Rutas("login")
    object Menu : Rutas("menu")
    object Panel : Rutas("panel")
}
