package com.tecsup.computerstore.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.computerstore.AppContainer
import com.tecsup.computerstore.domain.model.Item
import com.tecsup.computerstore.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritosViewModel(private val appContainer: AppContainer) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val uiState: StateFlow<UiState<List<Item>>> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                appContainer.itemRepository.getFavoritos().collect { _uiState.value = UiState.Exito(it) }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "No se pudieron leer los favoritos")
            }
        }
    }

    fun cambiarFavorito(item: Item) {
        viewModelScope.launch { appContainer.itemRepository.cambiarFavorito(item.id, !item.esFavorito) }
    }

    companion object {
        fun Factory(appContainer: AppContainer): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = FavoritosViewModel(appContainer) as T
        }
    }
}
