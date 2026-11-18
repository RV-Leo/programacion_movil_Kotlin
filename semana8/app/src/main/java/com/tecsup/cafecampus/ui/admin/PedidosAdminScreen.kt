package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.ui.components.EstadoChip
import com.tecsup.cafecampus.viewmodel.PedidosAdminViewModel
import java.util.Locale

private val Fondo = Color(0xFFF8F7FC)
private val Morado = Color(0xFF5546E8)

@Composable
fun PedidosAdminScreen(vm: PedidosAdminViewModel, onVolver: () -> Unit, onAbrirDetalle: (Int) -> Unit) {
    val pedidos by vm.pedidos.collectAsState()
    val seleccionado by vm.estadoSeleccionado.collectAsState()
    val estados = listOf(EstadoPedido.RECIBIDO, EstadoPedido.PREPARANDO, EstadoPedido.LISTO, EstadoPedido.ENTREGADO, EstadoPedido.CANCELADO)

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(horizontal = 16.dp, vertical = 10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver al panel")
            }
            Text("Pedidos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        androidx.compose.foundation.lazy.LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(estados) { estado ->
                FilterChip(
                    selected = seleccionado == estado,
                    onClick = { vm.seleccionarEstado(estado) },
                    label = { Text(estado.etiqueta()) },
                    shape = RoundedCornerShape(50.dp),
                    colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Morado,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        if (pedidos.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) { Text("No hay pedidos ${seleccionado.etiqueta().lowercase()}", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(pedidos, key = { it.pedido.id }) { pedido ->
                    PedidoAdminCard(
                        pedido = pedido,
                        onAvanzar = { vm.avanzar(pedido) },
                        onCancelar = { vm.cancelar(pedido) }, onAbrirDetalle = { onAbrirDetalle(pedido.pedido.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PedidoAdminCard(pedido: PedidoConDetalle, onAvanzar: () -> Unit, onCancelar: () -> Unit, onAbrirDetalle: () -> Unit) {
    val estado = EstadoPedido.desde(pedido.pedido.estado)
    val siguiente = estado.siguiente()
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(13.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("#${pedido.pedido.id.toString().padStart(4, '0')}  ${pedido.cliente?.nombre ?: "Cliente"}", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                EstadoChip(estado)
            }
            Text(
                pedido.detalles.joinToString(" · ") { "${it.cantidad} x ${it.nombreProducto}" },
                modifier = Modifier.padding(top = 5.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 7.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Recojo ${pedido.pedido.horaRecojo}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("S/ ${"%.2f".format(Locale.US, pedido.pedido.total)}", color = Morado, fontWeight = FontWeight.Bold)
            }
            Text(
                "Nota: ${pedido.pedido.nota.ifBlank { "Sin nota" }}",
                modifier = Modifier.padding(top = 5.dp),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF656B72)
            )
            if (siguiente != null) {
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = onAvanzar,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Morado),
                        shape = RoundedCornerShape(50.dp)
                    ) { Text("Pasar a ${siguiente.etiqueta()}") }
                    OutlinedButton(
                        onClick = onCancelar,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD34242)),
                        shape = RoundedCornerShape(50.dp)
                    ) { Text("Cancelar") }
                }
            }
        }
    }
}

private fun EstadoPedido.etiqueta(): String = when (this) {
    EstadoPedido.RECIBIDO -> "Nuevos"
    EstadoPedido.PREPARANDO -> "Preparando"
    EstadoPedido.LISTO -> "Listos"
    EstadoPedido.ENTREGADO -> "Entregados"
    EstadoPedido.CANCELADO -> "Cancelados"
}



