package com.ronda.tienda_tecsup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun TarjetaProducto(
    producto: Producto,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    var menuExpandido by remember { mutableStateOf(false) }

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text(producto.categoria, style = MaterialTheme.typography.bodyMedium)
            }
            Text(
                text = String.format(Locale.forLanguageTag("es-PE"), "S/ %.2f", producto.precio),
                style = MaterialTheme.typography.titleSmall
            )
            IconButton(onClick = { menuExpandido = !menuExpandido }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = if (menuExpandido) "Cerrar menú" else "Abrir menú",
                    tint = if (menuExpandido) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
