package com.ronda.tecsup_fit.data

import androidx.compose.runtime.mutableStateListOf

data class ReservaItem(
    val id: Int,
    val nombre: String,
    val fechaHora: String,
    val estado: String, // "Confirmada", "Completada", "Cancelada"
)

object ReservationRepository {
    val reservations = mutableStateListOf(
        ReservaItem(
            id = 1,
            nombre = "Cross Training",
            fechaHora = "Hoy, 6:00 pm",
            estado = "Confirmada",
        ),
        ReservaItem(
            id = 2,
            nombre = "Yoga funcional",
            fechaHora = "Ayer, 7:00 am",
            estado = "Completada",
        ),
    )

    var lastReservedName: String = "Cross Training"
    var lastReservedDetails: String = "Hoy, 6:00 pm · Sala 1"

    fun addReservation(nombre: String, hora: String, sala: String) {
        val nextId = (reservations.maxOfOrNull { it.id } ?: 0) + 1
        val fechaHora = "Hoy, $hora"
        lastReservedName = nombre
        lastReservedDetails = "Hoy, $hora · $sala"

        reservations.add(
            0,
            ReservaItem(
                id = nextId,
                nombre = nombre,
                fechaHora = fechaHora,
                estado = "Confirmada",
            ),
        )
    }

    fun cancelReservation(id: Int) {
        val index = reservations.indexOfFirst { it.id == id }
        if (index != -1) {
            val item = reservations[index]
            reservations[index] = item.copy(estado = "Cancelada")
        }
    }
}
