package com.tecsup.computerstore.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Operaciones DAO en Room (fuente unica de verdad)
@Dao
interface ItemDao {
    @Query("SELECT * FROM items")
    fun getItems(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getItemById(id: Int): ItemEntity?

    @Query("SELECT * FROM items WHERE esFavorito = 1")
    fun getFavoritos(): Flow<List<ItemEntity>>

    @Query("SELECT id FROM items WHERE esFavorito = 1")
    suspend fun getIdsFavoritos(): List<Int>

    @Query("SELECT COUNT(*) FROM items")
    suspend fun contarItems(): Int

    @Query("SELECT COUNT(*) FROM items WHERE esFavorito = 1")
    suspend fun contarFavoritos(): Int

    @Query("UPDATE items SET esFavorito = :esFavorito WHERE id = :id")
    suspend fun actualizarFavorito(id: Int, esFavorito: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ItemEntity)

    @Query("DELETE FROM items WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM items")
    suspend fun deleteAll()
}
