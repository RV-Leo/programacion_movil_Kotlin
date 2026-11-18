package com.tecsup.computerstore

import com.tecsup.computerstore.data.auth.AuthRepository
import com.tecsup.computerstore.data.repository.ItemRepository
import com.tecsup.computerstore.notification.Notificador

// Contenedor de dependencias de la app
interface AppContainer {
    val itemRepository: ItemRepository
    val authRepository: AuthRepository
    val notificador: Notificador
}
