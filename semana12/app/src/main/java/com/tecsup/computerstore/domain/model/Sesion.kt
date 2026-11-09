package com.tecsup.computerstore.domain.model

enum class Rol {
    CLIENTE,
    ADMIN
}

// Modelo de sesión de usuario
data class Sesion(
    val email: String,
    val usuario: String,
    val rol: Rol,
    val estaAutenticado: Boolean = true
)
