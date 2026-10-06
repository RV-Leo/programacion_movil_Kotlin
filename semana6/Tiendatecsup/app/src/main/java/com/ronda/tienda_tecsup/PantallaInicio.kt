package com.ronda.tienda_tecsup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaInicio(
    productos: List<Producto> = productosEjemplo,
    onProductoClick: (Producto) -> Unit
) {
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }
    val categorias = listOf("Todos") + categoriasProducto
    val productosFiltrados = if (categoriaSeleccionada == "Todos") {
        productos
    } else {
        productos.filter { it.categoria == categoriaSeleccionada }
    }
    val categoriasVisibles = if (categoriaSeleccionada == "Todos") {
        categoriasProducto
    } else {
        listOf(categoriaSeleccionada)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categorias) { categoria ->
                FilterChip(
                    selected = categoriaSeleccionada == categoria,
                    onClick = { categoriaSeleccionada = categoria },
                    label = { Text(categoria) }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            categoriasVisibles.forEach { categoria ->
                val productosCategoria = productosFiltrados.filter { it.categoria == categoria }
                if (productosCategoria.isNotEmpty()) {
                    item(key = "seccion-$categoria") {
                        Text(
                            text = categoria,
                            modifier = Modifier.padding(top = 8.dp),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    items(productosCategoria, key = { it.id }) { producto ->
                        TarjetaProducto(producto = producto) {
                            onProductoClick(producto)
                        }
                    }
                }
            }
        }
    }
}
