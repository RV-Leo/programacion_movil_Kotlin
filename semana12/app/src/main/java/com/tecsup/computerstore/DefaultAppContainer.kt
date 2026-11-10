package com.tecsup.computerstore

import android.content.Context
import com.tecsup.computerstore.data.local.AppDatabase
import com.tecsup.computerstore.data.auth.AuthRepository
import com.tecsup.computerstore.data.auth.FirebaseAuthRepository
import com.tecsup.computerstore.data.remote.RetrofitClient
import com.tecsup.computerstore.data.repository.ItemRepository
import com.tecsup.computerstore.data.repository.ItemRepositoryImpl
import com.tecsup.computerstore.notification.NotificadorAndroid
import com.tecsup.computerstore.notification.Notificador

// Implementacion por defecto del contenedor de dependencias
class DefaultAppContainer(context: Context) : AppContainer {

    private val apiService = RetrofitClient.crearApiService()
    private val database = AppDatabase.getInstance(context)

    override val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(apiService, database.itemDao())
    }

    override val authRepository: AuthRepository by lazy {
        FirebaseAuthRepository(context.applicationContext)
    }

    override val notificador: Notificador by lazy {
        NotificadorAndroid(context.applicationContext)
    }
}
