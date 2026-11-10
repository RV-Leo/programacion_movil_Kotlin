package com.tecsup.computerstore.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(viewModel: AdminViewModel, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val mensaje by viewModel.mensaje.collectAsStateWithLifecycle()
    Scaffold(topBar = {
        TopAppBar(title = { Text("Panel de administración") }, navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Volver") }
        })
    }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            when (val stats = state) {
                UiState.Cargando -> Cargando(Modifier.fillMaxWidth().weight(1f))
                is UiState.Error -> ErrorView(stats.mensaje, { viewModel.actualizarTotales() }, Modifier.fillMaxWidth().weight(1f))
                is UiState.Exito -> Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard("Productos guardados", stats.datos.items, Modifier.weight(1f))
                    StatCard("Favoritos", stats.datos.favoritos, Modifier.weight(1f))
                }
            }
            mensaje?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
            Spacer(Modifier.height(8.dp))
            Button(onClick = { viewModel.sincronizar() }, modifier = Modifier.fillMaxWidth()) { Text("Sincronizar") }
            OutlinedButton(onClick = { viewModel.limpiarCache() }, modifier = Modifier.fillMaxWidth()) { Text("Limpiar caché") }
        }
    }
}

@Composable
private fun StatCard(label: String, count: Int, modifier: Modifier = Modifier) {
    Card(modifier) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Text(count.toString(), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }
    }
}
