package com.ronda.campusgps.data
import com.ronda.campusgps.model.Posicion
interface UbicacionRepository { suspend fun obtenerUbicacion(): Posicion? }
