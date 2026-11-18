package com.tecsup.cafecampus.ui.cliente

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.ui.components.EstadoChip
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel
import java.util.Locale

private val Naranja = Color(0xFFEB5A0A)
private val Fondo = Color(0xFFFFFBF6)

@Composable
fun MisPedidosScreen(
    vm: PedidosClienteViewModel,
    onVolver: () -> Unit,
    onAbrirPedido: (Int) -> Unit
) {
    val pedidos by vm.misPedidos.collectAsState()
    var pestaña by remember { mutableStateOf("Activos") }
    val visibles = pedidos.filter { pedido ->
        val estado = EstadoPedido.desde(pedido.pedido.estado)
        if (pestaña == "Activos") estado in listOf(EstadoPedido.RECIBIDO, EstadoPedido.PREPARANDO, EstadoPedido.LISTO)
        else estado in listOf(EstadoPedido.ENTREGADO, EstadoPedido.CANCELADO)
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(18.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text("Mis pedidos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp).background(Color(0xFFF0EBE5), RoundedCornerShape(50.dp)).padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("Activos", "Historial").forEach { opcion ->
                FilterChip(
                    selected = pestaña == opcion,
                    onClick = { pestaña = opcion },
                    label = { Text(opcion) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50.dp),
                    colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        selectedLabelColor = Naranja
                    )
                )
            }
        }

        if (visibles.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(if (pestaña == "Activos") "No tienes pedidos activos" else "Aún no hay pedidos en tu historial", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(visibles, key = { it.pedido.id }) { pedido ->
                    PedidoClienteCard(pedido, onClick = { onAbrirPedido(pedido.pedido.id) })
                }
            }
        }
    }
}

@Composable
private fun PedidoClienteCard(pedido: PedidoConDetalle, onClick: () -> Unit) {
    val estado = EstadoPedido.desde(pedido.pedido.estado)
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Pedido #${pedido.pedido.id.toString().padStart(4, '0')}", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("›", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.titleLarge)
            }
            Text(
                pedido.detalles.joinToString(" · ") { "${it.nombreProducto} x${it.cantidad}" },
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
            Text("Recojo ${pedido.pedido.horaRecojo}", modifier = Modifier.padding(top = 4.dp), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(7.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                EstadoChip(estado)
                Text("S/ ${"%.2f".format(Locale.US, pedido.pedido.total)}", color = Naranja, fontWeight = FontWeight.Bold)
            }
        }
    }
}


