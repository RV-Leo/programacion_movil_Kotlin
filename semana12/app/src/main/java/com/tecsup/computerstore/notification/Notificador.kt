package com.tecsup.computerstore.notification

// Contrato para enviar notificaciones sin usar Context en ViewModel
interface Notificador {
    fun mostrarNotificacion(titulo: String, mensaje: String)
}
