package com.tecsup.computerstore.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.computerstore.AppContainer
import com.tecsup.computerstore.domain.model.Item
import com.tecsup.computerstore.ui.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ViewModel para administrar la lista de productos con datos de prueba
class ListaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val uiState: StateFlow<UiState<List<Item>>> = _uiState.asStateFlow()

    // Datos de prueba para la tienda de PC
    private val listaBase = mutableListOf(
        Item(1, "Laptop Gamer ASUS ROG", "Intel i7, 16GB RAM, RTX 4060", 4500.0, "Laptops", "", 10),
        Item(2, "Placa Madre MSI B550M", "Socket AM4, PCIe 4.0, DDR4", 550.0, "Placas", "", 15),
        Item(3, "Memoria RAM Corsair 16GB", "DDR4 3200MHz Vengeance RGB", 280.0, "RAM", "", 25),
        Item(4, "PC Armada Master Gaming", "Ryzen 7 5700X, 32GB RAM, RTX 4070", 6200.0, "PCs", "", 5),
        Item(5, "Teclado Mecanico Redragon", "Switch Red RGB, Anti-ghosting", 190.0, "Accesorios", "", 30),
        Item(6, "Laptop Lenovo Legion 5", "Ryzen 7, 16GB RAM, RTX 3060", 4100.0, "Laptops", "", 8)
    )

    init {
        cargarDatosPrueba()
    }

    fun cargarDatosPrueba() {
        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            delay(500)
            _uiState.value = UiState.Exito(listaBase.toList())
        }
    }

    fun cambiarFavorito(id: Int) {
        val index = listaBase.indexOfFirst { it.id == id }
        if (index != -1) {
            val itemActual = listaBase[index]
            listaBase[index] = itemActual.copy(esFavorito = !itemActual.esFavorito)
            _uiState.value = UiState.Exito(listaBase.toList())
        }
    }

    // Factory para instanciar ListaViewModel pasando el AppContainer
    companion object {
        fun Factory(appContainer: AppContainer): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ListaViewModel() as T
            }
        }
    }
}
