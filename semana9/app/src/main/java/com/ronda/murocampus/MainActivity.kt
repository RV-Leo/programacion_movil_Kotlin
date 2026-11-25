package com.ronda.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ronda.murocampus.data.PostRepositoryRemote
import com.ronda.murocampus.data.remote.RetrofitClient
import com.ronda.murocampus.ui.PantallaLista
import com.ronda.murocampus.ui.PantallaDetalle
import com.ronda.murocampus.ui.PantallaCrear
import com.ronda.murocampus.ui.theme.MuroCampusTheme
import com.ronda.murocampus.viewmodel.PostsViewModel
import com.ronda.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm: PostsViewModel = viewModel(
                factory = PostsViewModelFactory(PostRepositoryRemote(RetrofitClient.api))
            )
            val sistemaOscuro = isSystemInDarkTheme()
            var oscuro by rememberSaveable { mutableStateOf(sistemaOscuro) }
            MuroCampusTheme(darkTheme = oscuro) {
                MuroCampusApp(vm, oscuro) { oscuro = !oscuro }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MuroCampusApp(vm: PostsViewModel, oscuro: Boolean, cambiarTema: () -> Unit) {
    var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
    var creando by rememberSaveable { mutableStateOf(false) }
    BackHandler(enabled = idSeleccionado != null || creando) {
        if (creando) {
            creando = false
            vm.limpiarCreacion()
        } else {
            idSeleccionado = null
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                        Text(
                            text = "MuroCampus",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "COMUNIDAD UNIVERSITARIA",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    TextButton(onClick = cambiarTema) {
                        Text(
                            text = if (oscuro) "Claro" else "Oscuro",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (idSeleccionado == null && !creando) {
                ExtendedFloatingActionButton(
                    onClick = {
                        vm.limpiarCreacion()
                        creando = true
                    },
                    shape = RoundedCornerShape(28.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Text(
                        text = "Nueva publicación",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { innerPadding ->
        if (creando) {
            PantallaCrear(
                vm = vm,
                onBack = {
                    creando = false
                    vm.limpiarCreacion()
                },
                modifier = Modifier.padding(innerPadding)
            )
        } else if (idSeleccionado == null) {
            PantallaLista(
                vm = vm,
                onPostClick = { idSeleccionado = it },
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            PantallaDetalle(
                vm = vm,
                postId = idSeleccionado!!,
                onDeleted = { idSeleccionado = null },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MuroCampusTheme(darkTheme = false) {
        Scaffold { innerPadding ->
            Text(
                text = "MuroCampus",
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

