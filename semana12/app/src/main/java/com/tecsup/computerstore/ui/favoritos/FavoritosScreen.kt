package com.tecsup.computerstore.ui.favoritos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.computerstore.ui.UiState
import com.tecsup.computerstore.ui.components.Cargando
import com.tecsup.computerstore.ui.components.ErrorView
import com.tecsup.computerstore.ui.components.StoreProductCard
import com.tecsup.computerstore.ui.theme.StoreMuted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(viewModel: FavoritosViewModel, onBack: () -> Unit, onItemClick: (Int) -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Mis favoritos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") } }
            )
        }
    ) { padding ->
        when (val state = uiState) {
            UiState.Cargando -> Cargando(Modifier.fillMaxSize().padding(padding))
            is UiState.Error -> ErrorView(state.mensaje, onBack, Modifier.fillMaxSize().padding(padding))
            is UiState.Exito -> if (state.datos.isEmpty()) {
                Column(Modifier.fillMaxSize().padding(padding).padding(24.dp), verticalArrangement = Arrangement.Center) {
                    Text("Tu lista está esperando", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("Guarda aquí los productos que te interesan tocando el corazón.", modifier = Modifier.padding(top = 8.dp), color = StoreMuted)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.datos, key = { it.id }) { item ->
                        StoreProductCard(item, { onItemClick(item.id) }, { viewModel.cambiarFavorito(item) }, Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}
