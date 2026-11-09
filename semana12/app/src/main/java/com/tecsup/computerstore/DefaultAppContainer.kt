package com.tecsup.computerstore

import com.tecsup.computerstore.data.auth.AuthRepository
import com.tecsup.computerstore.data.remote.RetrofitClient
import com.tecsup.computerstore.data.repository.ItemRepository
import com.tecsup.computerstore.data.repository.ItemRepositoryImpl
import com.tecsup.computerstore.domain.model.Sesion
import com.tecsup.computerstore.notification.Notificador
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

// Implementacion por defecto del contenedor de dependencias
class DefaultAppContainer : AppContainer {

    private val apiService = RetrofitClient.crearApiService()

    override val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(apiService)
    }

    override val authRepository: AuthRepository by lazy {
        object : AuthRepository {
            override fun getSesionActual(): Flow<Sesion?> = MutableStateFlow(null)
            override suspend fun login(email: String, clave: String): Result<Sesion> =
                Result.failure(Exception("Sin implementar"))
            override suspend fun logout() {}
        }
    }

    override val notificador: Notificador by lazy {
        object : Notificador {
            override fun mostrarNotificacion(titulo: String, mensaje: String) {}
        }
    }
}
