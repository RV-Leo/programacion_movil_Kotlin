package com.tecsup.cafecampus.ui.cliente

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.ui.components.EstadoChip
import com.tecsup.cafecampus.ui.components.StepperEstado
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel
import java.util.Locale

private val Naranja = Color(0xFFEB5A0A)
private val Fondo = Color(0xFFFFFBF6)

@Composable
fun SeguimientoScreen(
    vm: PedidosClienteViewModel,
    pedidoId: Int,
    onVolver: () -> Unit,
    onMisPedidos: () -> Unit
) {
    val pedidoFlow = remember(vm, pedidoId) { vm.pedido(pedidoId) }
    val resultado by pedidoFlow.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(18.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text("Seguimiento", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        if (resultado == null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) { CircularProgressIndicator(color = Naranja) }
            return
        }

        val pedido = resultado!!.pedido
        val estado = EstadoPedido.desde(pedido.estado)
        Card(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Naranja)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Pedido #${pedido.id.toString().padStart(4, '0')}", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text("Recojo a las ${pedido.horaRecojo}", color = Color.White)
                Text("Tiempo estimado: 10 min", color = Color(0xFFFFE5D2), style = MaterialTheme.typography.bodySmall)
            }
        }

        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(top = 22.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (estado == EstadoPedido.CANCELADO) EstadoChip(estado) else StepperEstado(estado)
        }

        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(Modifier.fillMaxWidth().padding(14.dp)) {
                Text(
                    resultado!!.detalles.joinToString(" · ") { "${it.cantidad} x ${it.nombreProducto}" },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "Total  S/ ${"%.2f".format(Locale.US, pedido.total)}",
                    modifier = Modifier.padding(top = 5.dp),
                    color = Naranja,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Button(
            onClick = onMisPedidos,
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Naranja)
        ) { Text("Ver mis pedidos") }
    }
}
