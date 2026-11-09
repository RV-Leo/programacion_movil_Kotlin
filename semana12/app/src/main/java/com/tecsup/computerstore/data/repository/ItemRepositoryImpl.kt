package com.tecsup.computerstore.data.repository

import com.tecsup.computerstore.data.mapper.toDomain
import com.tecsup.computerstore.data.remote.ApiService
import com.tecsup.computerstore.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// Implementacion del repositorio que consume la API remota
class ItemRepositoryImpl(
    private val apiService: ApiService
) : ItemRepository {

    private val _itemsFlow = MutableStateFlow<List<Item>>(emptyList())

    override fun getItems(): Flow<List<Item>> = _itemsFlow.asStateFlow()

    override fun getFavoritos(): Flow<List<Item>> {
        val favoritos = _itemsFlow.value.filter { it.esFavorito }
        return MutableStateFlow(favoritos).asStateFlow()
    }

    override suspend fun getItemById(id: Int): Item? {
        return try {
            apiService.getItemById(id).toDomain()
        } catch (e: Exception) {
            _itemsFlow.value.find { it.id == id }
        }
    }

    override suspend fun sincronizarConRemoto() {
        val respuesta = apiService.getItems()
        val itemsDominio = respuesta.products.map { it.toDomain() }
        _itemsFlow.value = itemsDominio
    }

    override suspend fun cambiarFavorito(id: Int, esFavorito: Boolean) {
        _itemsFlow.value = _itemsFlow.value.map { item ->
            if (item.id == id) item.copy(esFavorito = esFavorito) else item
        }
    }

    override suspend fun agregarItem(item: Item) {
        _itemsFlow.value = _itemsFlow.value + item
    }

    override suspend fun eliminarItem(id: Int) {
        _itemsFlow.value = _itemsFlow.value.filter { it.id != id }
    }
}
