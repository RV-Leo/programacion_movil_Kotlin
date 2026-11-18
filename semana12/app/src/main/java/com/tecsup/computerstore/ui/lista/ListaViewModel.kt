package com.tecsup.computerstore.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.computerstore.AppContainer
import com.tecsup.computerstore.data.repository.ItemRepository
import com.tecsup.computerstore.domain.model.Item
import com.tecsup.computerstore.notification.Notificador
import com.tecsup.computerstore.ui.UiState
import java.io.IOException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ViewModel conectado al ItemRepository para consumir la API remota
class ListaViewModel(
    private val repository: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val uiState: StateFlow<UiState<List<Item>>> = _uiState.asStateFlow()

    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    private var cargaInicialTerminada = false

    init {
        viewModelScope.launch {
            repository.getItems().collect { lista ->
                if (lista.isNotEmpty() || cargaInicialTerminada) {
                    _uiState.value = UiState.Exito(lista)
                }
            }
        }
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            val guardados = repository.getItems().first()
            if (guardados.isEmpty()) _uiState.value = UiState.Cargando
            try {
                val descargados = repository.sincronizarConRemoto()
                cargaInicialTerminada = true
                _aviso.value = null
                _uiState.value = UiState.Exito(repository.getItems().first())
                notificador.mostrarNotificacion("Datos actualizados", "Se guardaron $descargados elementos")
            } catch (e: Exception) {
                cargaInicialTerminada = true
                val locales = repository.getItems().first()
                if (locales.isEmpty()) {
                    _uiState.value = UiState.Error(mensajeDe(e))
                } else {
                    _uiState.value = UiState.Exito(locales)
                    _aviso.value = "Sin conexión: mostrando datos guardados"
                }
            }
        }
    }

    fun limpiarAviso() { _aviso.value = null }

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

    companion object {
        fun Factory(appContainer: AppContainer): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ListaViewModel(appContainer.itemRepository, appContainer.notificador) as T
            }
        }
    }

    private fun mensajeDe(e: Throwable): String = when (e) {
        is IOException -> "No hay conexión a internet. Revisa tu red y vuelve a intentarlo."
        is retrofit2.HttpException -> "El servidor respondió con error ${e.code()}."
        else -> e.message ?: "Ocurrió un error al cargar los productos."
    }
}
