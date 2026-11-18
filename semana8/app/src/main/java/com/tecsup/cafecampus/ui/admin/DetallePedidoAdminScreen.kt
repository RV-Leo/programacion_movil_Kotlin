package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.tecsup.cafecampus.viewmodel.PedidosAdminViewModel
import java.util.Locale

private val MoradoDetalleAdmin = Color(0xFF5546E8)
private val FondoDetalleAdmin = Color(0xFFF8F7FC)

@Composable
fun DetallePedidoAdminScreen(vm: PedidosAdminViewModel, pedidoId: Int, onVolver: () -> Unit) {
    val pedidoFlow = remember(vm, pedidoId) { vm.pedido(pedidoId) }
    val resultado by pedidoFlow.collectAsState()
    var estadoElegido by remember(pedidoId, resultado?.pedido?.estado) {
        mutableStateOf(resultado?.pedido?.estado?.let(EstadoPedido::desde)?.siguiente())
    }

    if (resultado == null) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            CircularProgressIndicator(color = MoradoDetalleAdmin)
        }
        return
    }

    val actual = EstadoPedido.desde(resultado!!.pedido.estado)
    val estadosEditables = listOf(EstadoPedido.RECIBIDO, EstadoPedido.PREPARANDO, EstadoPedido.LISTO, EstadoPedido.ENTREGADO)
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState()).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver") }
            Text("Detalle del pedido", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(17.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(Modifier.fillMaxWidth().padding(15.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text("Pedido #${resultado!!.pedido.id.toString().padStart(4, '0')}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    EstadoChip(actual)
                }
                Text("Hoy · Recojo ${resultado!!.pedido.horaRecojo}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(17.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(Modifier.fillMaxWidth().padding(15.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Cliente", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("${resultado!!.cliente?.nombre ?: "Cliente"} · Estudiante", fontWeight = FontWeight.SemiBold)
                androidx.compose.material3.HorizontalDivider()
                resultado!!.detalles.forEach { detalle ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${detalle.nombreProducto}   x${detalle.cantidad}")
                        Text("S/ ${"%.2f".format(Locale.US, detalle.precioUnitario * detalle.cantidad)}", fontWeight = FontWeight.SemiBold)
                    }
                }
                androidx.compose.material3.HorizontalDivider()
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total", fontWeight = FontWeight.Bold)
                    Text("S/ ${"%.2f".format(Locale.US, resultado!!.pedido.total)}", color = MoradoDetalleAdmin, fontWeight = FontWeight.Bold)
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth().background(Color(0xFFFFF0BF), RoundedCornerShape(15.dp)).padding(14.dp)
        ) {
            Text("Nota del cliente", fontWeight = FontWeight.Bold, color = Color(0xFF846113))
            Text(resultado!!.pedido.nota.ifBlank { "Sin nota para la cocina" }, color = Color(0xFF765E20), modifier = Modifier.padding(top = 4.dp))
        }

        Text("Cambiar estado", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        androidx.compose.foundation.lazy.LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(estadosEditables.size) { index ->
                val estado = estadosEditables[index]
                FilterChip(
                    selected = (estadoElegido ?: actual) == estado,
                    onClick = { estadoElegido = estado },
                    label = { Text(estado.etiquetaAdmin()) },
                    colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MoradoDetalleAdmin,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
        val siguiente = estadoElegido ?: actual.siguiente()
        if (siguiente != null && actual != EstadoPedido.ENTREGADO && actual != EstadoPedido.CANCELADO) {
            Button(
                onClick = { vm.cambiarEstado(resultado!!, siguiente) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MoradoDetalleAdmin)
            ) { Text("Pasar a ${siguiente.etiquetaAdmin()}", fontWeight = FontWeight.Bold) }
        }
        if (actual != EstadoPedido.ENTREGADO && actual != EstadoPedido.CANCELADO) {
            OutlinedButton(
                onClick = { vm.cancelar(resultado!!) },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD34242))
            ) { Text("Cancelar pedido") }
        }
        Spacer(Modifier.height(8.dp))
    }
}

private fun EstadoPedido.etiquetaAdmin(): String = when (this) {
    EstadoPedido.RECIBIDO -> "Recibido"
    EstadoPedido.PREPARANDO -> "Preparando"
    EstadoPedido.LISTO -> "Listo"
    EstadoPedido.ENTREGADO -> "Entregado"
    EstadoPedido.CANCELADO -> "Cancelado"
}

