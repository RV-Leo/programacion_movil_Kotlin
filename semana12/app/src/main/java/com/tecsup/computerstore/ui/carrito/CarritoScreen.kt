package com.tecsup.computerstore.ui.carrito

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.computerstore.ui.components.ItemImage
import java.util.Locale

@Composable
fun CarritoScreen(
    lineas: List<LineaCarrito>,
    onBrowseProducts: () -> Unit,
    onQuantityChange: (Int, Int) -> Unit,
    onRemove: (Int) -> Unit,
    onCheckout: () -> Unit
) {
    val total = lineas.sumOf { it.subtotal }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (lineas.isNotEmpty()) {
                Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 8.dp) {
                    Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Total", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(formatSoles(total), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        }
                        Button(onClick = onCheckout, modifier = Modifier.fillMaxWidth().padding(top = 10.dp), shape = RoundedCornerShape(14.dp)) {
                            Text("Ir a pagar")
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (lineas.isEmpty()) {
            Column(
                Modifier.fillMaxSize().padding(padding).padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.RemoveShoppingCart, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(bottom = 20.dp).size(72.dp))
                Text("Tu carrito está vacío", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Explora el catálogo y agrega los productos que te interesan.", modifier = Modifier.padding(top = 8.dp, bottom = 22.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                Button(onClick = onBrowseProducts) { Text("Ver productos") }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Column(Modifier.padding(bottom = 4.dp)) {
                        Text("Tu carrito", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text("${lineas.sumOf { it.cantidad }} productos", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                items(lineas, key = { it.item.id }) { linea ->
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(18.dp)) {
                        Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ItemImage(
                                item = linea.item,
                                modifier = Modifier.weight(.8f).height(92.dp).clip(RoundedCornerShape(14.dp))
                            )
                            Column(Modifier.weight(2f)) {
                                Text(linea.item.nombre, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                                Text(formatSoles(linea.item.precio), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = { onQuantityChange(linea.item.id, linea.cantidad - 1) }, enabled = linea.cantidad > 1) { Icon(Icons.Default.Remove, "Reducir") }
                                    Text(linea.cantidad.toString(), fontWeight = FontWeight.Bold)
                                    IconButton(onClick = { onQuantityChange(linea.item.id, linea.cantidad + 1) }, enabled = linea.cantidad < linea.item.stock) { Icon(Icons.Default.Add, "Aumentar") }
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                IconButton(onClick = { onRemove(linea.item.id) }) { Icon(Icons.Default.DeleteOutline, "Quitar producto", tint = MaterialTheme.colorScheme.error) }
                                Text(formatSoles(linea.subtotal), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                item { Spacer(Modifier.height(8.dp)) }
            }
        }
    }
}

private fun formatSoles(value: Double): String = "S/ ${String.format(Locale("es", "PE"), "%.2f", value)}"
