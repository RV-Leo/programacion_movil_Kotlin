package com.tecsup.computerstore.ui

// Estado generico para la UI en ViewModels
sealed interface UiState<out T> {
    data object Cargando : UiState<Nothing>
    data class Exito<out T>(val datos: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}
