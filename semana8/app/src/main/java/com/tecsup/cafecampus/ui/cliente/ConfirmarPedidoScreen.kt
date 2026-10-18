package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.model.ItemCarrito
import com.tecsup.cafecampus.ui.components.imagenProducto
import com.tecsup.cafecampus.viewmodel.MenuViewModel
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel
import java.util.Locale

private val Fondo = Color(0xFFFFFBF6)
private val Naranja = Color(0xFFEB5A0A)

@Composable
fun ConfirmarPedidoScreen(
    pedidosVm: PedidosClienteViewModel,
    menuVm: MenuViewModel,
    recibirPedidos: Boolean,
    onVolver: () -> Unit,
    onPedidoConfirmado: (Long) -> Unit
) {
    val carrito by menuVm.carrito.collectAsState()
    val total by menuVm.total.collectAsState()
    val confirmando by pedidosVm.confirmando.collectAsState()
    val error by pedidosVm.error.collectAsState()
    var horaRecojo by remember { mutableStateOf("10:30") }
    var pago by remember { mutableStateOf("Caja") }
    var nota by remember { mutableStateOf("") }
    val horas = listOf("Ahora", "10:30", "11:00", "11:30")
    val subtotal = carrito.sumOf { it.producto.precio * it.cantidad }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(horizontal = 18.dp, vertical = 10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text("Confirmar pedido", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        if (carrito.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Tu carrito está vacío", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(12.dp))
                OutlinedButton(onClick = onVolver) { Text("Seguir pidiendo") }
            }
            return
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(MaterialTheme.colorScheme.surface).padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    carrito.forEach { item -> LineaPedido(item) }
                    Text("Total de productos: ${carrito.sumOf { it.cantidad }}", color = Color(0xFF888888), style = MaterialTheme.typography.bodySmall)
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("Hora de recojo", fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        horas.forEach { hora ->
                            FilterChip(
                                selected = horaRecojo == hora,
                                onClick = { horaRecojo = hora },
                                label = { Text(hora) },
                                colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Naranja,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("Forma de pago", fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Caja", "Yape / Plin").forEach { opcion ->
                            FilterChip(
                                selected = pago == opcion,
                                onClick = { pago = opcion },
                                label = { Text(if (opcion == "Caja") "Pagar en caja" else opcion) },
                                colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFFFFE8D7),
                                    selectedLabelColor = Naranja
                                )
                            )
                        }
                    }
                }
            }
            item {
                OutlinedTextField(
                    value = nota,
                    onValueChange = { nota = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nota para la cocina (opcional)") },
                    placeholder = { Text("Sin ají, por favor") },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    minLines = 2,
                    maxLines = 3,
                    shape = RoundedCornerShape(14.dp)
                )
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surface).padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    FilaPrecio("Subtotal", subtotal)
                    FilaPrecio("Servicio", 0.0)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total", fontWeight = FontWeight.Bold)
                        Text("S/ ${"%.2f".format(Locale.US, total)}", color = Naranja, fontWeight = FontWeight.Bold)
                    }
                }
            }
            if (error != null) item { Text(error!!, color = MaterialTheme.colorScheme.error) }
        }

        if (!recibirPedidos) {
            Text("Cafetería cerrada: vuelve a intentarlo cuando se habiliten los pedidos.", color = MaterialTheme.colorScheme.error)
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                pedidosVm.confirmar(carrito, horaRecojo, pago, nota) { pedidoId ->
                    menuVm.vaciar()
                    onPedidoConfirmado(pedidoId)
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = !confirmando && carrito.isNotEmpty() && recibirPedidos,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Naranja)
        ) {
            if (confirmando) CircularProgressIndicator(modifier = Modifier.size(22.dp), color = Color.White)
            else Text(if (recibirPedidos) "Confirmar pedido" else "Cafetería cerrada", fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(6.dp))
        OutlinedButton(onClick = onVolver, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(12.dp)) {
            Text("Seguir pidiendo", color = Naranja)
        }
    }
}

@Composable
private fun LineaPedido(item: ItemCarrito) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(imagenProducto(item.producto.nombre)),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp))
        )
        Column(modifier = Modifier.weight(1f).padding(horizontal = 10.dp)) {
            Text(item.producto.nombre, fontWeight = FontWeight.SemiBold, maxLines = 1)
            Text("x${item.cantidad}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text(
            "S/ ${"%.2f".format(Locale.US, item.producto.precio * item.cantidad)}",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun FilaPrecio(etiqueta: String, monto: Double) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(etiqueta, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("S/ ${"%.2f".format(Locale.US, monto)}", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
