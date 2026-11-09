package com.tecsup.computerstore.data.remote.dto

import com.google.gson.annotations.SerializedName

// DTO que responde la API REST
data class ItemDto(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("precio") val precio: Double,
    @SerializedName("categoria") val categoria: String,
    @SerializedName("imagenUrl") val imagenUrl: String,
    @SerializedName("stock") val stock: Int,
    @SerializedName("esFavorito") val esFavorito: Boolean = false
)
