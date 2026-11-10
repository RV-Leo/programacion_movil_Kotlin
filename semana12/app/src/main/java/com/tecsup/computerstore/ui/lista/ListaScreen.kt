package com.tecsup.computerstore.ui.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.computerstore.domain.model.Item
import com.tecsup.computerstore.ui.UiState
import com.tecsup.computerstore.ui.components.Cargando
import com.tecsup.computerstore.ui.components.ErrorView

// Pantalla principal de catalogo observando el estado con collectAsStateWithLifecycle
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(
    viewModel: ListaViewModel,
    onItemClick: (Int) -> Unit = {},
    onFavoritosClick: () -> Unit = {},
    usuario: String = "Leo",
    esAdmin: Boolean = false,
    onAdminClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hola, $usuario") },
                actions = {
                    IconButton(onClick = onFavoritosClick) {
                        Icon(Icons.Default.Favorite, contentDescription = "Ver favoritos")
                    }
                    if (esAdmin) IconButton(onClick = onAdminClick) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = "Panel de administración")
                    }
                    IconButton(onClick = onLogout) { Icon(Icons.Default.Logout, contentDescription = "Cerrar sesión") }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (aviso != null) {
                Surface(color = MaterialTheme.colorScheme.secondaryContainer, modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(aviso.orEmpty(), Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                        Button(onClick = { viewModel.cargarProductos() }) { Text("Reintentar") }
                    }
                }
            }
            when (val estado = uiState) {
                is UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorView(
                    mensaje = estado.mensaje,
                    onReintentar = { viewModel.cargarProductos() }
                )
                is UiState.Exito -> ListaContenido(
                    items = estado.datos,
                    onFavoritoClick = { viewModel.cambiarFavorito(it) },
                    onItemClick = onItemClick
                )
            }
        }
    }
}

// Lista de tarjetas de productos
@Composable
private fun ListaContenido(
    items: List<Item>,
    onFavoritoClick: (Int) -> Unit,
    onItemClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items, key = { it.id }) { item ->
            ItemCard(
                item = item,
                onFavoritoClick = { onFavoritoClick(item.id) },
                onClick = { onItemClick(item.id) }
            )
        }
    }
}

// Tarjeta individual para cada producto de la tienda
@Composable
private fun ItemCard(
    item: Item,
    onFavoritoClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.nombre,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = item.categoria,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.descripcion,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "S/ ${item.precio}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            IconButton(onClick = onFavoritoClick) {
                Icon(
                    imageVector = if (item.esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (item.esFavorito) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
