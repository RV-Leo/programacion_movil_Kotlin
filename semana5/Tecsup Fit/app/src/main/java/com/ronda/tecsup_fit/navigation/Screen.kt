package com.ronda.tecsup_fit.navigation

sealed class Screen(val route: String) {

    object Home : Screen("home")

    object Detail : Screen("detail/{itemId}") {
        fun createRoute(itemId: Int) = "detail/$itemId"
    }

    object Confirmation : Screen("confirmation")

    object Reservations : Screen("reservations")

    object Routines : Screen("routines")

    object Profile : Screen("profile")
}