package com.tecsup.computerstore.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.computerstore.AppContainer
import com.tecsup.computerstore.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AdminStats(val items: Int, val favoritos: Int)

class AdminViewModel(private val appContainer: AppContainer) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<AdminStats>>(UiState.Cargando)
    val uiState: StateFlow<UiState<AdminStats>> = _uiState.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    init { actualizarTotales() }

    fun actualizarTotales() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Exito(
                    AdminStats(
                        items = appContainer.itemRepository.cantidadGuardada(),
                        favoritos = appContainer.itemRepository.cantidadFavoritos()
                    )
                )
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "No se pudieron consultar los totales")
            }
        }
    }

    fun sincronizar() {
        viewModelScope.launch {
            _mensaje.value = null
            try {
                val cantidad = appContainer.itemRepository.sincronizarConRemoto()
                appContainer.notificador.mostrarNotificacion("Datos actualizados", "Se guardaron $cantidad elementos")
                _mensaje.value = "Se guardaron $cantidad elementos."
                actualizarTotales()
            } catch (e: Exception) {
                _mensaje.value = e.message ?: "No se pudo sincronizar. Revisa la conexión."
            }
        }
    }

    fun limpiarCache() {
        viewModelScope.launch {
            try {
                appContainer.itemRepository.limpiarCache()
                _mensaje.value = "Caché eliminada. Los totales están en cero."
                actualizarTotales()
            } catch (e: Exception) {
                _mensaje.value = e.message ?: "No se pudo limpiar la caché."
            }
        }
    }
}

object AdminViewModelFactory {
    fun create(appContainer: AppContainer): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = AdminViewModel(appContainer) as T
    }
}
