package com.ronda.tienda_tecsup

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun TarjetaProducto(
    producto: Producto,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    favorito: Boolean = false,
    onCambiarFavorito: () -> Unit = {}
) {
    var menuExpandido by remember { mutableStateOf(false) }
    val context = LocalContext.current

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
            androidx.compose.foundation.layout.Box {
                IconButton(onClick = { menuExpandido = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Más opciones",
                        tint = if (menuExpandido) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                DropdownMenu(
                    expanded = menuExpandido,
                    onDismissRequest = { menuExpandido = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(if (favorito) "Quitar de Favoritos" else "Favoritos") },
                        leadingIcon = {
                            Icon(
                                imageVector = if (favorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null
                            )
                        },
                        onClick = {
                            onCambiarFavorito()
                            menuExpandido = false
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))
                    DropdownMenuItem(
                        text = { Text("Compartir") },
                        leadingIcon = {
                            Icon(Icons.Default.Share, contentDescription = null)
                        },
                        onClick = {
                            val compartir = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "${producto.nombre} (${producto.categoria}) - " +
                                        String.format(Locale.forLanguageTag("es-PE"), "S/ %.2f", producto.precio)
                                )
                            }
                            context.startActivity(Intent.createChooser(compartir, "Compartir producto"))
                            menuExpandido = false
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))
                    DropdownMenuItem(
                        text = { Text("Reportar") },
                        leadingIcon = {
                            Icon(Icons.Default.Flag, contentDescription = null)
                        },
                        onClick = {
                            Toast.makeText(context, "Producto reportado", Toast.LENGTH_SHORT).show()
                            menuExpandido = false
                        }
                    )
                }
            }
        }
    }
}
