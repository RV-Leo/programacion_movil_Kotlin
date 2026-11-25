package com.ronda.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ronda.murocampus.viewmodel.PostsViewModel
import com.ronda.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(
    vm: PostsViewModel,
    postId: Int,
    onDeleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    var confirmarEliminacion by remember(postId) { mutableStateOf(false) }
    val eliminacion by vm.eliminacion.collectAsStateWithLifecycle()
    LaunchedEffect(postId) {
        vm.limpiarEliminacion()
        vm.abrir(postId)
    }
    LaunchedEffect(eliminacion) {
        if (eliminacion is UiState.Exito) onDeleted()
    }
    val estado by vm.detalle.collectAsStateWithLifecycle()
    Box(modifier.fillMaxSize()) {
        when (val actual = estado) {
            UiState.Cargando -> Cargando("Cargando publicación...")
            is UiState.Error -> ErrorConReintento(actual.mensaje) { vm.abrir(postId) }
            is UiState.Exito -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Usuario ${actual.datos.post.userId}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary)
                        Text(actual.datos.post.title, style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold)
                        Text(actual.datos.post.body, style = MaterialTheme.typography.bodyLarge)
                        Text("Comentarios (${actual.datos.comentarios.size})",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(top = 12.dp))
                        OutlinedButton(
                            onClick = { confirmarEliminacion = true },
                            enabled = eliminacion !is UiState.Cargando
                        ) { Text("Eliminar publicación") }
                        if (eliminacion is UiState.Error) {
                            Text((eliminacion as UiState.Error).mensaje,
                                color = MaterialTheme.colorScheme.error)
                            TextButton(onClick = { vm.eliminar(postId) }) { Text("Reintentar") }
                        }
                    }
                }
                items(actual.datos.comentarios, key = { it.id }) { comentario ->
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(comentario.email, color = Color(0xFF1565C0),
                            style = MaterialTheme.typography.labelLarge)
                        Text(comentario.body, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
    if (confirmarEliminacion) {
        AlertDialog(
            onDismissRequest = { confirmarEliminacion = false },
            title = { Text("Eliminar publicación") },
            text = { Text("¿Quieres quitar esta publicación del muro?") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarEliminacion = false
                    vm.eliminar(postId)
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { confirmarEliminacion = false }) { Text("Cancelar") }
            }
        )
    }
}
