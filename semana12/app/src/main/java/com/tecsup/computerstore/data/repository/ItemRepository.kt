package com.tecsup.computerstore.data.repository

import com.tecsup.computerstore.domain.model.Item
import kotlinx.coroutines.flow.Flow

// Contrato del repositorio de productos
interface ItemRepository {
    fun getItems(): Flow<List<Item>>
    fun getFavoritos(): Flow<List<Item>>
    suspend fun getItemById(id: Int): Item?
    suspend fun sincronizarConRemoto(): Int
    suspend fun cambiarFavorito(id: Int, esFavorito: Boolean)
    suspend fun agregarItem(item: Item)
    suspend fun eliminarItem(id: Int)
    suspend fun cantidadGuardada(): Int
    suspend fun cantidadFavoritos(): Int
    suspend fun limpiarCache()
}
