package com.ronda.centrotareas.data

interface TareasRepository {
    suspend fun contarPrimos(hasta: Int): Int
}
