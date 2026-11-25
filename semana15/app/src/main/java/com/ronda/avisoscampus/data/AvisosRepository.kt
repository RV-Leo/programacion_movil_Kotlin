package com.ronda.avisoscampus.data

import com.ronda.avisoscampus.model.Aviso

interface AvisosRepository {
    suspend fun registrar(aviso: Aviso)
}

class AvisosRepositoryLocal : AvisosRepository {
    private val avisos = mutableListOf<Aviso>()

    override suspend fun registrar(aviso: Aviso) {
        avisos += aviso
    }
}
