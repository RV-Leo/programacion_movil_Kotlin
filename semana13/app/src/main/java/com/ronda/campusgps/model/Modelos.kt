package com.ronda.campusgps.model

data class Posicion(val latitud: Double, val longitud: Double)
data class Lugar(val id: Int, val nombre: String, val categoria: String, val latitud: Double, val longitud: Double)
data class LugarConDistancia(val lugar: Lugar, val metros: Float?)
enum class TipoError { SIN_PERMISO, GPS_APAGADO, SIN_PLAY_SERVICES, OTRO }
sealed interface UbicacionUiState {
    data object Inicial : UbicacionUiState
    data object Buscando : UbicacionUiState
    data class Lista(val posicion: Posicion) : UbicacionUiState
    data class Error(val mensaje: String, val tipo: TipoError = TipoError.OTRO) : UbicacionUiState
}
