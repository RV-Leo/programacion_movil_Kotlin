package com.ronda.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Usuario #${actual.datos.post.userId}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = actual.datos.post.title,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = actual.datos.post.body,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        OutlinedButton(
                            onClick = { confirmarEliminacion = true },
                            enabled = eliminacion !is UiState.Cargando,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .height(50.dp),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text("Eliminar publicación", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold)
                        }
                        if (eliminacion is UiState.Error) {
                            Text((eliminacion as UiState.Error).mensaje, color = MaterialTheme.colorScheme.error)
                            TextButton(onClick = { vm.eliminar(postId) }) { Text("Reintentar eliminación") }
                        }
                        
                        Text(
                            text = "Comentarios (${actual.datos.comentarios.size})",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                items(actual.datos.comentarios, key = { it.id }) { comentario ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = comentario.email,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = comentario.body,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
    if (confirmarEliminacion) {
        AlertDialog(
            onDismissRequest = { confirmarEliminacion = false },
            shape = RoundedCornerShape(28.dp),
            title = { Text("Eliminar publicación", fontWeight = FontWeight.Bold) },
            text = { Text("¿Estás seguro de que quieres quitar esta publicación del muro de MuroCampus?") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarEliminacion = false
                    vm.eliminar(postId)
                }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmarEliminacion = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

