package com.tecsup.computerstore.data.remote.dto

import com.google.gson.annotations.SerializedName

// DTO de producto mapeado desde la API dummyjson.com
data class ItemDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val nombre: String,
    @SerializedName("description") val descripcion: String,
    @SerializedName("price") val precio: Double,
    @SerializedName("category") val categoria: String,
    @SerializedName("thumbnail") val imagenUrl: String,
    @SerializedName("stock") val stock: Int,
    @SerializedName("esFavorito") val esFavorito: Boolean = false
)
