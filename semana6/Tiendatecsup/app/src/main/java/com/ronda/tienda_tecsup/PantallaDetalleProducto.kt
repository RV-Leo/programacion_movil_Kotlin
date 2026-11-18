package com.ronda.tienda_tecsup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun PantallaDetalleProducto(
    producto: Producto?,
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (producto != null) {
            Text(producto.nombre, style = MaterialTheme.typography.headlineSmall)
            Text("Categoría: ${producto.categoria}", style = MaterialTheme.typography.bodyLarge)
            Text(
                String.format(Locale.forLanguageTag("es-PE"), "Precio: S/ %.2f", producto.precio),
                style = MaterialTheme.typography.titleMedium
            )
        } else {
            Text("Producto no encontrado", style = MaterialTheme.typography.titleLarge)
        }
        Button(onClick = onVolver) {
            Text("Volver")
        }
    }
}
