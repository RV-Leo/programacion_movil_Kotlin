package com.tecsup.computerstore.data.auth

import com.tecsup.computerstore.domain.model.Sesion
import kotlinx.coroutines.flow.Flow

// Contrato para la autenticacion de usuarios
interface AuthRepository {
    fun getSesionActual(): Flow<Sesion?>
    suspend fun login(email: String, clave: String): Result<Sesion>
    suspend fun registrar(nombre: String, email: String, clave: String): Result<Sesion>
    suspend fun logout()
}
