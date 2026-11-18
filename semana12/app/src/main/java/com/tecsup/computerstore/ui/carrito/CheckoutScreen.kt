package com.tecsup.computerstore.ui.carrito

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.tecsup.computerstore.ui.theme.StoreMuted
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    lineas: List<LineaCarrito>,
    correo: String,
    onConfirmOrder: suspend (String) -> Unit,
    onBack: () -> Unit,
    onOrderComplete: () -> Unit
) {
    var direccion by rememberSaveable { mutableStateOf("") }
    var mostrarConfirmacion by rememberSaveable { mutableStateOf(false) }
    var guardandoPedido by rememberSaveable { mutableStateOf(false) }
    var errorPedido by rememberSaveable { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val total = lineas.sumOf { it.subtotal }

    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Pedido de demostración listo") },
            text = { Text("No se realizó ningún cobro. Esta versión todavía no tiene una pasarela de pago conectada.") },
            confirmButton = { TextButton(onClick = onOrderComplete) { Text("Ver mis pedidos") } }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Revisar pedido", fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Volver al carrito") } }
            )
        },
        bottomBar = {
            if (lineas.isNotEmpty()) {
                Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 8.dp) {
                    Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total a pagar", color = StoreMuted)
                            Text(formatSoles(total), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = {
                                guardandoPedido = true
                                errorPedido = null
                                scope.launch {
                                    runCatching { onConfirmOrder(direccion.trim()) }
                                        .onSuccess { mostrarConfirmacion = true }
                                        .onFailure { errorPedido = it.localizedMessage ?: "No se pudo guardar el pedido." }
                                    guardandoPedido = false
                                }
                            },
                            enabled = direccion.isNotBlank() && !guardandoPedido,
                            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) { if (guardandoPedido) CircularProgressIndicator() else Text("Registrar pedido de prueba") }
                    }
                }
            }
        }
    ) { padding ->
        if (lineas.isEmpty()) {
            Column(Modifier.fillMaxSize().padding(padding).padding(24.dp), verticalArrangement = Arrangement.Center) {
                Text("No hay productos para pagar", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                TextButton(onClick = onBack) { Text("Volver al carrito") }
            }
        } else {
            Column(
                Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Resumen de compra", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(18.dp)) {
                    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        lineas.forEach { linea ->
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("${linea.cantidad} × ${linea.item.nombre}", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                                Text(formatSoles(linea.subtotal), fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 12.dp))
                            }
                        }
                    }
                }
                Text("Datos de entrega", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                errorPedido?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                OutlinedTextField(
                    value = correo,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Correo de la cuenta") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Dirección de entrega") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    placeholder = { Text("Av., calle, distrito y referencia") },
                    minLines = 2,
                    maxLines = 3
                )
                Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(16.dp)) {
                    Column(Modifier.fillMaxWidth().padding(16.dp)) {
                        Text("Pago de demostración", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text(
                            "Puedes revisar el pedido y probar el flujo. No se procesa un pago real ni se envía un pedido a una tienda.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(top = 5.dp)
                        )
                    }
                }
                Spacer(Modifier.height(90.dp))
            }
        }
    }
}

private fun formatSoles(value: Double): String = "S/ ${String.format(Locale("es", "PE"), "%.2f", value)}"
