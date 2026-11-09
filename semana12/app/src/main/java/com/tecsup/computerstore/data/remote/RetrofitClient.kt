package com.tecsup.computerstore.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Cliente Retrofit singleton configurado con la URL Base de dummyjson.com
object RetrofitClient {
    private const val BASE_URL = "https://dummyjson.com/"

    fun crearApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
