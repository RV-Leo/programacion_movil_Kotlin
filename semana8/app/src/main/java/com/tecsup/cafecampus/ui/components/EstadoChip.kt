package com.tecsup.cafecampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.model.EstadoPedido

@Composable
fun EstadoChip(estado: EstadoPedido) {
    val (texto, fondo, tinta) = when (estado) {
        EstadoPedido.RECIBIDO -> Triple("Recibido", Color(0xFFE8F5EE), Color(0xFF238454))
        EstadoPedido.PREPARANDO -> Triple("Preparando", Color(0xFFFFF1D5), Color(0xFFB87600))
        EstadoPedido.LISTO -> Triple("Listo", Color(0xFFE0F6E8), Color(0xFF218957))
        EstadoPedido.ENTREGADO -> Triple("Entregado", Color(0xFFE9ECEF), Color(0xFF626B73))
        EstadoPedido.CANCELADO -> Triple("Cancelado", Color(0xFFFFE6E6), Color(0xFFBC3A3A))
    }
    Row(
        modifier = Modifier.background(fondo, RoundedCornerShape(50.dp)).padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(texto, color = tinta, fontSize = 11.sp)
    }
}
