package com.tecsup.computerstore.ui.procesos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.computerstore.data.repository.OrderRecord
import com.tecsup.computerstore.data.repository.OrderRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcesosScreen(repository: OrderRepository, uid: String, esAdmin: Boolean) {
    var pedidos by remember { mutableStateOf<List<OrderRecord>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    fun cargar() {
        scope.launch {
            cargando = true; error = null
            runCatching { if (esAdmin) repository.listAll() else repository.listForUser(uid) }
                .onSuccess { pedidos = it }
                .onFailure { error = it.localizedMessage ?: "No se pudieron cargar los pedidos." }
            cargando = false
        }
    }
    LaunchedEffect(uid, esAdmin) { cargar() }
    val totalDemo = pedidos.filter { it.status != "Cancelado" }.sumOf { it.total }
    Scaffold(topBar = {
        TopAppBar(title = { Text(if (esAdmin) "Pedidos y ventas" else "Mis procesos") }, actions = {
            IconButton(onClick = ::cargar) { Icon(Icons.Default.Refresh, "Actualizar") }
        })
    }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
            if (esAdmin) {
                Card(Modifier.fillMaxWidth().padding(vertical = 10.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Resumen de demostración", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("${pedidos.size} pedidos · S/ ${"%.2f".format(Locale("es", "PE"), totalDemo)} registrados")
                        Text("Importes de prueba; no son cobros reales.", style = MaterialTheme.typography.bodySmall)
                    }
                }
            } else Text("Consulta aquí el estado de tus pedidos.", Modifier.padding(vertical = 16.dp), style = MaterialTheme.typography.bodyMedium)
            when {
                cargando -> CircularProgressIndicator(Modifier.padding(24.dp))
                error != null -> Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(error!!, color = MaterialTheme.colorScheme.error)
                    Button(onClick = ::cargar) { Text("Reintentar") }
                }
                pedidos.isEmpty() -> Text(if (esAdmin) "Todavía no hay pedidos." else "Aún no tienes pedidos registrados.", Modifier.padding(vertical = 24.dp))
                else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(pedidos, key = { it.id }) { pedido ->
                        PedidoCard(pedido, esAdmin) { estado ->
                            scope.launch {
                                runCatching { repository.updateStatus(pedido.id, estado) }
                                    .onSuccess { cargar() }
                                    .onFailure { error = it.localizedMessage ?: "No se pudo actualizar el pedido." }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PedidoCard(pedido: OrderRecord, esAdmin: Boolean, onStatus: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Pedido #${pedido.id.takeLast(7)}", fontWeight = FontWeight.Bold)
                Text("S/ ${"%.2f".format(Locale("es", "PE"), pedido.total)}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            if (esAdmin) Text("${pedido.customer} · ${pedido.email}", style = MaterialTheme.typography.bodySmall)
            Text(pedido.items, style = MaterialTheme.typography.bodyMedium)
            Text("Entrega: ${pedido.address}", style = MaterialTheme.typography.bodySmall)
            Text("${pedido.status} · ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "PE")).format(Date(pedido.createdAt))}", color = MaterialTheme.colorScheme.secondary)
            if (esAdmin) {
                Button(onClick = { expanded = true }) { Text("Actualizar estado") }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    listOf("Pendiente", "En preparación", "Enviado", "Entregado", "Cancelado").forEach { estado ->
                        DropdownMenuItem(text = { Text(estado) }, onClick = { expanded = false; onStatus(estado) })
                    }
                }
            }
        }
    }
}
