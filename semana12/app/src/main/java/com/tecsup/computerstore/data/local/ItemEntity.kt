package com.tecsup.computerstore.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabla de productos en Room
@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val categoria: String,
    val imagenUrl: String,
    val stock: Int,
    val esFavorito: Boolean = false
)
