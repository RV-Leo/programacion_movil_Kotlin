package com.tecsup.cafecampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.model.EstadoPedido

private val Naranja = Color(0xFFEB5A0A)

@Composable
fun StepperEstado(estado: EstadoPedido) {
    val pasos = listOf(
        "Recibido" to "Tu pedido llegó a la cocina",
        "Preparando" to "Están cocinando tu pedido",
        "Listo" to "Puedes pasar a recogerlo",
        "Entregado" to "¡Buen provecho!"
    )
    val actual = when (estado) {
        EstadoPedido.RECIBIDO -> 0
        EstadoPedido.PREPARANDO -> 1
        EstadoPedido.LISTO -> 2
        EstadoPedido.ENTREGADO -> 3
        EstadoPedido.CANCELADO -> -1
    }

    if (estado == EstadoPedido.CANCELADO) {
        EstadoChip(estado)
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
        pasos.forEachIndexed { index, (titulo, detalle) ->
            val completado = index < actual
            val seleccionado = index == actual
            val color = when {
                completado -> Color(0xFF20A263)
                seleccionado -> Naranja
                else -> Color(0xFFD4D9DE)
            }
            Row(verticalAlignment = Alignment.Top) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier.size(22.dp).background(color, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (completado) Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                        else if (seleccionado) Box(Modifier.size(9.dp).background(Color.White, CircleShape))
                    }
                    if (index < pasos.lastIndex) {
                        Box(Modifier.padding(vertical = 2.dp).size(width = 2.dp, height = 38.dp).background(if (index < actual) Color(0xFF20A263) else Color(0xFFE2E5E8)))
                    }
                }
                Column(modifier = Modifier.padding(start = 12.dp, top = 1.dp, bottom = 10.dp)) {
                    Text(titulo, fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.SemiBold, color = if (index <= actual) Color(0xFF30343B) else Color(0xFF9AA1A8))
                    Text(detalle, fontSize = 12.sp, color = Color(0xFF8A9096))
                }
            }
        }
    }
}
