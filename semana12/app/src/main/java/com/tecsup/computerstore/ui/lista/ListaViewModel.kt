package com.tecsup.computerstore.ui.lista

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

// ViewModel conectado al ItemRepository para consumir la API remota
class ListaViewModel(
    private val repository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val uiState: StateFlow<UiState<List<Item>>> = _uiState.asStateFlow()

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            try {
                repository.sincronizarConRemoto()
                repository.getItems().collect { lista ->
                    _uiState.value = UiState.Exito(lista)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Error al cargar la lista desde la API")
            }
        }
    }

    fun cambiarFavorito(id: Int) {
        viewModelScope.launch {
            val estadoActual = _uiState.value
            if (estadoActual is UiState.Exito) {
                val item = estadoActual.datos.find { it.id == id }
                item?.let {
                    repository.cambiarFavorito(id, !it.esFavorito)
                }
            }
        }
    }

    // Factory para instanciar ListaViewModel pasando el AppContainer
    companion object {
        fun Factory(appContainer: AppContainer): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ListaViewModel(appContainer.itemRepository) as T
            }
        }
    }
}
