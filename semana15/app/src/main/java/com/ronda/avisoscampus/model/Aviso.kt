package com.ronda.avisoscampus.model

data class Aviso(
    val id: Int,
    val titulo: String,
    val texto: String,
    val origen: OrigenAviso
)

enum class OrigenAviso {
    BOTON,
    RECORDATORIO
}
