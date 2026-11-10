package com.tecsup.computerstore.ui.favoritos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(viewModel: FavoritosViewModel, onBack: () -> Unit, onItemClick: (Int) -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(topBar = {
        TopAppBar(title = { Text("Mis favoritos") }, navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Volver") }
        })
    }) { padding ->
        when (val state = uiState) {
            UiState.Cargando -> Cargando(Modifier.fillMaxSize().padding(padding))
            is UiState.Error -> ErrorView(state.mensaje, onBack, Modifier.fillMaxSize().padding(padding))
            is UiState.Exito -> if (state.datos.isEmpty()) {
                Column(Modifier.fillMaxSize().padding(padding).padding(24.dp), verticalArrangement = Arrangement.Center) {
                    Text("Aún no tienes favoritos", style = MaterialTheme.typography.titleLarge)
                    Text("Marca productos con el corazón para guardarlos aquí.", modifier = Modifier.padding(top = 8.dp))
                }
            } else {
                LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(state.datos, key = { it.id }) { item -> FavoriteItem(item, { onItemClick(item.id) }, { viewModel.cambiarFavorito(item) }) }
                }
            }
        }
    }
}

@Composable
private fun FavoriteItem(item: Item, onClick: () -> Unit, onRemove: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(Modifier.weight(1f)) {
                Text(item.nombre, style = MaterialTheme.typography.titleMedium)
                Text(item.categoria, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text("S/ ${"%.2f".format(item.precio)}", modifier = Modifier.padding(top = 6.dp), style = MaterialTheme.typography.titleSmall)
            }
            IconButton(onClick = onRemove) { Icon(Icons.Default.Favorite, "Quitar de favoritos", tint = MaterialTheme.colorScheme.error) }
        }
    }
}
