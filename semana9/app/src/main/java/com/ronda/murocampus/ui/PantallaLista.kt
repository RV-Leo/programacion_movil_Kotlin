package com.ronda.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ronda.murocampus.viewmodel.PostsViewModel
import com.ronda.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(vm: PostsViewModel, onPostClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    val estado by vm.listaVisible.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()
    Box(modifier.fillMaxSize()) {
        when (val actual = estado) {
            UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorConReintento(actual.mensaje, vm::cargar)
            is UiState.Exito -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = busqueda,
                        onValueChange = vm::buscarTitulo,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Buscar por título") },
                        singleLine = true
                    )
                }
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed((listOf<Int?>(null) + (1..10).map { it })) { _, userId ->
                            FilterChip(
                                selected = filtro == userId,
                                onClick = { vm.filtrar(userId) },
                                label = { Text(userId?.let { "Usuario $it" } ?: "Todos") }
                            )
                        }
                    }
                }
                if (actual.datos.isEmpty()) {
                    item { Text("Sin publicaciones") }
                } else {
                    items(actual.datos, key = { it.id }) { post ->
                        TarjetaPost(post = post, onClick = { onPostClick(post.id) })
                    }
                }
            }
        }
    }
}
