package com.tecsup.computerstore.ui.detalle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.computerstore.domain.model.Item
import com.tecsup.computerstore.ui.UiState
import com.tecsup.computerstore.ui.components.Cargando
import com.tecsup.computerstore.ui.components.ErrorView

// Pantalla de detalle observando el estado con collectAsStateWithLifecycle
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    viewModel: DetalleViewModel,
    onVolverClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Producto") },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            when (val estado = uiState) {
                is UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorView(
                    mensaje = estado.mensaje,
                    onReintentar = { viewModel.cargarDetalle() }
                )
                is UiState.Exito -> DetalleContenido(
                    item = estado.datos,
                    onComprarClick = { viewModel.comprarProducto() }
                )
            }
        }
    }
}

// Vista con la informacion detallada del producto
@Composable
private fun DetalleContenido(
    item: Item,
    onComprarClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = item.nombre,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Categoria: ${item.categoria}",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = item.descripcion,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Stock disponible: ${item.stock} unidades",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Precio: S/ ${item.precio}",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onComprarClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Comprar Ahora")
        }
    }
}
