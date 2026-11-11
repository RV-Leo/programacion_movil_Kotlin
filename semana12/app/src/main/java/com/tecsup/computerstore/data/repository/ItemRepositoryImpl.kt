package com.tecsup.computerstore.data.repository

import com.tecsup.computerstore.data.local.ItemDao
import com.tecsup.computerstore.data.mapper.toDomain
import com.tecsup.computerstore.data.mapper.toDomainList
import com.tecsup.computerstore.data.mapper.toEntity
import com.tecsup.computerstore.data.remote.ApiService
import com.tecsup.computerstore.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Retrofit actualiza Room; las pantallas siempre leen el catálogo desde Room. */
class ItemRepositoryImpl(
    private val apiService: ApiService,
    private val itemDao: ItemDao
) : ItemRepository {

    override fun getItems(): Flow<List<Item>> = itemDao.getItems().map { it.toDomainList() }

    override fun getFavoritos(): Flow<List<Item>> = itemDao.getFavoritos().map { it.toDomainList() }

    override suspend fun getItemById(id: Int): Item? {
        val cached = itemDao.getItemById(id)
        if (cached != null) return cached.toDomain()
        return runCatching {
            apiService.getItemById(id).toEntity().also { itemDao.insert(it) }.toDomain()
        }.getOrNull()
    }

    override suspend fun sincronizarConRemoto(): Int {
        val idsFavoritos = itemDao.getIdsFavoritos().toSet()
        val productosLocales = DemoCatalogo.productos.map { producto ->
            producto.copy(esFavorito = producto.id in idsFavoritos).toEntity()
        }
        // Inserta primero el catálogo base para que la app tenga productos incluso sin red.
        itemDao.insertAll(productosLocales)

        val respuesta = apiService.getItems()
        val categoriasCompatibles = setOf("laptops", "tablets", "mobile-accessories")
        val productosRemotos = respuesta.products
            .filter { it.categoria in categoriasCompatibles }
            .map { producto ->
                val categoria = when (producto.categoria) {
                    "laptops" -> "Laptops"
                    "tablets" -> "Tablets"
                    else -> "Accesorios"
                }
                producto.toEntity(esFavorito = producto.id in idsFavoritos).copy(categoria = categoria)
            }
        itemDao.insertAll(productosRemotos)
        return productosLocales.size + productosRemotos.size
    }

    override suspend fun cambiarFavorito(id: Int, esFavorito: Boolean) {
        itemDao.actualizarFavorito(id, esFavorito)
    }

    override suspend fun agregarItem(item: Item) = itemDao.insert(item.toEntity())

    override suspend fun eliminarItem(id: Int) = itemDao.deleteById(id)

    override suspend fun cantidadGuardada(): Int = itemDao.contarItems()

    override suspend fun cantidadFavoritos(): Int = itemDao.contarFavoritos()

    override suspend fun limpiarCache() = itemDao.deleteAll()
}
