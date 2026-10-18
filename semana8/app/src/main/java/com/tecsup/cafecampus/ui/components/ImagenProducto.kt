package com.tecsup.cafecampus.ui.components

import com.tecsup.cafecampus.R
import java.text.Normalizer
import java.util.Locale

fun imagenProducto(nombre: String): Int {
    val nombreNormalizado = Normalizer.normalize(nombre.lowercase(Locale.ROOT), Normalizer.Form.NFD)
        .replace("\\p{Mn}+".toRegex(), "")
        .trim()

    return when (nombreNormalizado) {
        "cafe americano" -> R.drawable.cafe_americano
        "cafe con leche" -> R.drawable.cafe_leche
        "capuchino" -> R.drawable.capuchino
        "sandwich de pollo" -> R.drawable.sandwich_pollo
        "empanada de carne" -> R.drawable.empanada_carne
        "muffin de arandanos" -> R.drawable.muffin_arandanos
        else -> R.drawable.cafe_americano
    }
}

