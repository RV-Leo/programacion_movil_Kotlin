package com.ronda.tienda_tecsup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaFavoritos(
    favoritoIds: Set<Int>,
    onProductoClick: (Producto) -> Unit,
    onCambiarFavorito: (Producto) -> Unit
) {
    val favoritos = productosEjemplo.filter { it.id in favoritoIds }

    if (favoritos.isEmpty()) {
        Text(
            text = "Todavía no tienes productos favoritos.",
            modifier = Modifier.padding(24.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Favoritos", style = MaterialTheme.typography.titleLarge)
            }
            items(favoritos, key = { it.id }) { producto ->
                TarjetaProducto(
                    producto = producto,
                    favorito = true,
                    onClick = { onProductoClick(producto) },
                    onCambiarFavorito = { onCambiarFavorito(producto) }
                )
            }
        }
    }
}
