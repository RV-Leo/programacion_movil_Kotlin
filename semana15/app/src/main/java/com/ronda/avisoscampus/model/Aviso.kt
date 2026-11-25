package com.ronda.avisoscampus.model

data class Aviso(
    val id: Int,
    val titulo: String,
    val texto: String,
    val origen: OrigenAviso,
    val hora: Long = System.currentTimeMillis()
)

enum class OrigenAviso(val texto: String) {
    BOTON("Botón"),
    RECORDATORIO("Recordatorio"),
    MODO_AVION("Modo avión")
}
