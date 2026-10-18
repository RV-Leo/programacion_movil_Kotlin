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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.ui.components.EstadoChip
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.viewmodel.PanelAdminViewModel
import com.tecsup.cafecampus.viewmodel.PedidosAdminViewModel
import java.util.Locale

private val FondoPanel = Color(0xFFF8F7FC)
private val Morado = Color(0xFF5546E8)

@Composable
fun PanelScreen(
    vm: PanelAdminViewModel,
    pedidosVm: PedidosAdminViewModel,
    onVerPedidos: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val nuevos by vm.nuevos.collectAsState()
    val preparando by vm.preparando.collectAsState()
    val listos by vm.listos.collectAsState()
    val ventasHoy by vm.ventasHoy.collectAsState()
    val pedidosHoy by vm.pedidosHoy.collectAsState()
    val pedidosNuevos by pedidosVm.pedidos.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().background(FondoPanel).verticalScroll(rememberScrollState()).padding(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Panel de control", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Cafetería · Hoy", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onCerrarSesion) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
            }
        }

        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(9.dp), modifier = Modifier.fillMaxWidth()) {
            ContadorCard("Nuevos", nuevos, Color(0xFFE1ECFF), Color(0xFF2868D8), Modifier.weight(1f))
            ContadorCard("Preparando", preparando, Color(0xFFFFF1C9), Color(0xFFBD7900), Modifier.weight(1f))
            ContadorCard("Listos", listos, Color(0xFFDDF8E9), Color(0xFF258854), Modifier.weight(1f))
        }

        Spacer(Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Morado)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Ventas del día", color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.labelLarge)
                    Text("S/ ${"%.2f".format(Locale.US, ventasHoy)}", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                }
                Text("$pedidosHoy pedidos", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text("Nuevos pedidos", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Button(onClick = onVerPedidos, colors = ButtonDefaults.textButtonColors(contentColor = Morado)) {
                Text("Ver todos")
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.padding(start = 4.dp))
            }
        }

        if (pedidosNuevos.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Text("No hay pedidos nuevos", modifier = Modifier.padding(16.dp), color = Color.Gray)
            }
        } else {
            pedidosNuevos.take(4).forEach { pedido ->
                PedidoNuevoResumen(pedido = pedido, onPreparar = { pedidosVm.avanzar(pedido) })
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun ContadorCard(
    titulo: String,
    valor: Int,
    fondo: Color,
    tinta: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.background(fondo, RoundedCornerShape(16.dp)).padding(vertical = 13.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(valor.toString(), color = tinta, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(titulo, color = tinta, fontSize = 11.sp)
    }
}

@Composable
private fun PedidoNuevoResumen(pedido: PedidoConDetalle, onPreparar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("#${pedido.pedido.id.toString().padStart(4, '0')}  ${pedido.cliente?.nombre ?: "Cliente"}", fontWeight = FontWeight.SemiBold)
                Text(
                    pedido.detalles.joinToString(" · ") { "${it.cantidad} x ${it.nombreProducto}" },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1
                )
                Text("Recojo ${pedido.pedido.horaRecojo} · S/ ${"%.2f".format(Locale.US, pedido.pedido.total)}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Button(
                onClick = onPreparar,
                colors = ButtonDefaults.buttonColors(containerColor = Morado),
                shape = RoundedCornerShape(50.dp)
            ) { Text(if (EstadoPedido.desde(pedido.pedido.estado).siguiente() == EstadoPedido.PREPARANDO) "Preparar" else "Avanzar") }
        }
    }
}

