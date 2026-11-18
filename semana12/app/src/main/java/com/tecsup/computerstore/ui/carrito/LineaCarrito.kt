package com.tecsup.computerstore.ui.carrito

import com.tecsup.computerstore.domain.model.Item

data class LineaCarrito(val item: Item, val cantidad: Int) {
    val subtotal: Double get() = item.precio * cantidad
}
