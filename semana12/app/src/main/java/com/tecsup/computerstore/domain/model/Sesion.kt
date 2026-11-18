package com.tecsup.computerstore.domain.model

enum class Rol {
    USUARIO,
    ADMIN
}

// Modelo de sesión de usuario
data class Sesion(
    val uid: String,
    val email: String,
    val usuario: String,
    val rol: Rol,
    val estaAutenticado: Boolean = true
)
