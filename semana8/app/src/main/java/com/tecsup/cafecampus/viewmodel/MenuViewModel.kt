package com.tecsup.cafecampus.viewmodel

import com.tecsup.cafecampus.data.local.entity.Producto
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow

class MenuViewModel(private val repo: CafeRepository) : ViewModel() {
    val productos: StateFlow<List<Producto>> = repo.observarProductos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
// TODO (Paso 7): carrito: agregar, quitar, total, vaciar
}