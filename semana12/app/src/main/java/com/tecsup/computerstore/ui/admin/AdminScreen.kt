package com.tecsup.computerstore.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
fun AdminScreen(viewModel: AdminViewModel, onBack: () -> Unit, onProcessesClick: () -> Unit) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val mensaje by viewModel.mensaje.collectAsStateWithLifecycle()
    Scaffold(topBar = {
        TopAppBar(title = { Text("Panel de administración") }, navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Volver") }
        })
    }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(24.dp)) {
                Row(Modifier.fillMaxWidth().padding(20.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Hola, administrador", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text("Gestiona el catálogo y los pedidos de la tienda", style = MaterialTheme.typography.bodyMedium)
                    }
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            }
            Text("Resumen", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            when (val stats = state) {
                UiState.Cargando -> Cargando(Modifier.fillMaxWidth().weight(1f))
                is UiState.Error -> ErrorView(stats.mensaje, { viewModel.actualizarTotales() }, Modifier.fillMaxWidth().weight(1f))
                is UiState.Exito -> Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard("Productos", stats.datos.items, Icons.Default.Inventory2, Modifier.weight(1f))
                    StatCard("Favoritos", stats.datos.favoritos, Icons.Default.Favorite, Modifier.weight(1f))
                }
            }
            mensaje?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
            Text("Operaciones", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Button(onClick = onProcessesClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                Icon(Icons.Default.ReceiptLong, contentDescription = null)
                Text("  Gestionar pedidos y ventas")
            }
            OutlinedButton(onClick = { viewModel.sincronizar() }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                Icon(Icons.Default.Sync, contentDescription = null)
                Text("  Sincronizar catálogo")
            }
            OutlinedButton(onClick = { viewModel.limpiarCache() }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                Icon(Icons.Default.DeleteSweep, contentDescription = null)
                Text("  Limpiar caché local")
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun StatCard(label: String, count: Int, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Text(count.toString(), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }
    }
}
