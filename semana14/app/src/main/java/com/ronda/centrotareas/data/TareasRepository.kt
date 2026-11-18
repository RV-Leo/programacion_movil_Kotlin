package com.ronda.centrotareas.data

import kotlinx.coroutines.flow.Flow
import com.ronda.centrotareas.model.ProgresoPrimos

interface TareasRepository {
    suspend fun contarPrimos(hasta: Int): Int
    fun contarPrimosConProgreso(hasta: Int): Flow<ProgresoPrimos>
    fun progresoTarea(): Flow<Float>
    suspend fun cargarPerfil(): String
    suspend fun cargarPedidos(simularError: Boolean): List<String>
}
