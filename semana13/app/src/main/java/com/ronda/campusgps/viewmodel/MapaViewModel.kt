package com.ronda.campusgps.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ronda.campusgps.data.*
import com.ronda.campusgps.model.*
import com.ronda.campusgps.util.distanciaMetros
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MapaViewModel(private val ubicacionRepo: UbicacionRepository, private val lugaresRepo: LugaresRepository) : ViewModel() {
    private val _estado = MutableStateFlow<UbicacionUiState>(UbicacionUiState.Inicial)
    val estado = _estado.asStateFlow()
    private val _lugares = MutableStateFlow(lugaresRepo.obtenerLugares().map { LugarConDistancia(it, null) })
    val lugares = _lugares.asStateFlow()
    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado = _seleccionado.asStateFlow()
    val masCercano: StateFlow<Lugar?> = lugares.map { it.firstOrNull { item -> item.metros != null }?.lugar }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun buscar() {
        viewModelScope.launch {
            _estado.value = UbicacionUiState.Buscando
            if (!ubicacionRepo.playServicesDisponible()) {
                _estado.value = UbicacionUiState.Error("Este dispositivo no tiene Google Play Services", TipoError.SIN_PLAY_SERVICES); return@launch
            }
            try {
                val pos = ubicacionRepo.obtenerUbicacion()
                if (pos == null) _estado.value = UbicacionUiState.Error("Activa el GPS e intenta de nuevo", TipoError.GPS_APAGADO)
                else { actualizarDistancias(pos); _estado.value = UbicacionUiState.Lista(pos) }
            } catch (_: SecurityException) { permisoDenegado() }
            catch (_: Exception) { _estado.value = UbicacionUiState.Error("Activa el GPS e intenta de nuevo", TipoError.GPS_APAGADO) }
        }
    }
    fun permisoDenegado() { _estado.value = UbicacionUiState.Error("Sin permiso de ubicación no puedo mostrar dónde estás", TipoError.SIN_PERMISO) }
    fun seleccionar(id: Int?) { _seleccionado.value = id }
    private fun actualizarDistancias(pos: Posicion) { _lugares.value = lugaresRepo.obtenerLugares().map { LugarConDistancia(it, distanciaMetros(pos,it)) }.sortedBy { it.metros } }
}
