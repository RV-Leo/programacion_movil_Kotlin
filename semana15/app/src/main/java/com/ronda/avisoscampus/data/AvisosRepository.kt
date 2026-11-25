package com.ronda.avisoscampus.data

import com.ronda.avisoscampus.model.Aviso
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface AvisosRepository {
    val avisos: StateFlow<List<Aviso>>
    suspend fun registrar(aviso: Aviso)
}

class AvisosRepositoryLocal : AvisosRepository {
    private val _avisos = MutableStateFlow<List<Aviso>>(emptyList())
    override val avisos: StateFlow<List<Aviso>> = _avisos.asStateFlow()

    override suspend fun registrar(aviso: Aviso) {
        _avisos.value = listOf(aviso) + _avisos.value
    }
}
