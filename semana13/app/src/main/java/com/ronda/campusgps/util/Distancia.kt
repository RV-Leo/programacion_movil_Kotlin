package com.ronda.campusgps.util
import android.location.Location
import com.ronda.campusgps.model.Lugar
import com.ronda.campusgps.model.Posicion
fun distanciaMetros(desde: Posicion, hasta: Lugar): Float {
    val out = FloatArray(1)
    Location.distanceBetween(desde.latitud, desde.longitud, hasta.latitud, hasta.longitud, out)
    return out[0]
}
fun formatearDistancia(metros: Float): String = if (metros < 1000) "${metros.toInt()} m" else "${"%.1f".format(metros / 1000)} km"
