package com.tecsup.computerstore.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.computerstore.AppContainer
import com.tecsup.computerstore.data.repository.ItemRepository
import com.tecsup.computerstore.domain.model.Item
import com.tecsup.computerstore.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ViewModel para administrar el detalle del producto mediante el segundo endpoint
class DetalleViewModel(
    private val itemId: Int,
    private val repository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Item>>(UiState.Cargando)
    val uiState: StateFlow<UiState<Item>> = _uiState.asStateFlow()

    init {
        cargarDetalle()
    }

    fun cargarDetalle() {
        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            try {
                val item = repository.getItemById(itemId)
                if (item != null) {
                    _uiState.value = UiState.Exito(item)
                } else {
                    _uiState.value = UiState.Error("Producto no encontrado")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Error al cargar el detalle")
            }
        }
    }

    fun cambiarFavorito() {
        val estado = _uiState.value as? UiState.Exito<Item> ?: return
        val nuevoEstado = !estado.datos.esFavorito
        _uiState.value = UiState.Exito(estado.datos.copy(esFavorito = nuevoEstado))
        viewModelScope.launch {
            repository.cambiarFavorito(itemId, nuevoEstado)
        }
    }


    // Factory para instanciar DetalleViewModel
    companion object {
        fun Factory(itemId: Int, appContainer: AppContainer): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DetalleViewModel(itemId, appContainer.itemRepository) as T
            }
        }
    }
}
