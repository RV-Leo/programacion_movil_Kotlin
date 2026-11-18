package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MenuViewModel(private val repo: CafeRepository) : ViewModel() {
    val productos: StateFlow<List<Producto>> = repo.observarProductos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _carrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carrito: StateFlow<List<ItemCarrito>> = _carrito.asStateFlow()

    val total: StateFlow<Double> = carrito
        .map { items -> items.sumOf { it.producto.precio * it.cantidad } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val cantidadEnCarrito: StateFlow<Int> = carrito
        .map { items -> items.sumOf { it.cantidad } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun agregar(producto: Producto, cantidad: Int = 1, nota: String = "") {
        if (!producto.disponible || cantidad <= 0) return

        _carrito.update { items ->
            val existente = items.firstOrNull { it.producto.id == producto.id }
            if (existente == null) {
                items + ItemCarrito(producto = producto, cantidad = cantidad, nota = nota.trim())
            } else {
                items.map { item ->
                    if (item.producto.id == producto.id) {
                        item.copy(
                            cantidad = item.cantidad + cantidad,
                            nota = nota.trim().ifEmpty { item.nota }
                        )
                    } else item
                }
            }
        }
    }

    fun quitar(productoId: Int) {
        _carrito.update { items -> items.filterNot { it.producto.id == productoId } }
    }

    fun vaciar() {
        _carrito.value = emptyList()
    }
}
