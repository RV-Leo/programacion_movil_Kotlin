package com.tecsup.computerstore.domain.model

// Modelo principal de la tienda para la UI
data class Item(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val categoria: String,
    val imagenUrl: String,
    val stock: Int,
    val esFavorito: Boolean = false
)
