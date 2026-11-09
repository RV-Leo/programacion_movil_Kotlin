package com.tecsup.computerstore.data.remote

import com.tecsup.computerstore.data.remote.dto.ItemDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Interfaz Retrofit para llamadas a la API
interface ApiService {
    @GET("items")
    suspend fun getItems(): List<ItemDto>

    @GET("items/{id}")
    suspend fun getItemById(@Path("id") id: Int): ItemDto

    @POST("items")
    suspend fun agregarItem(@Body item: ItemDto): ItemDto
}
