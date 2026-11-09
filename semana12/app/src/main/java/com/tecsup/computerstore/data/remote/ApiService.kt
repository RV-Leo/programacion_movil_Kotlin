package com.tecsup.computerstore.data.remote

import com.tecsup.computerstore.data.remote.dto.ItemDto
import com.tecsup.computerstore.data.remote.dto.ProductResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

// Interfaz Retrofit para consumir la API de dummyjson.com
interface ApiService {
    @GET("products/category/laptops")
    suspend fun getItems(): ProductResponseDto

    @GET("products/{id}")
    suspend fun getItemById(@Path("id") id: Int): ItemDto
}
