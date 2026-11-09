package com.tecsup.computerstore.data.remote.dto

import com.google.gson.annotations.SerializedName

// DTO contenedor de la respuesta de la API dummyjson.com
data class ProductResponseDto(
    @SerializedName("products") val products: List<ItemDto>
)
