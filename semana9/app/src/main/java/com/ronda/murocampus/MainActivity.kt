package com.ronda.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ronda.murocampus.data.PostRepositoryRemote
import com.ronda.murocampus.data.remote.RetrofitClient
import com.ronda.murocampus.ui.PantallaLista
import com.ronda.murocampus.ui.PantallaDetalle
import com.ronda.murocampus.ui.theme.MuroCampusTheme
import com.ronda.murocampus.viewmodel.PostsViewModel
import com.ronda.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MuroCampusTheme {
                val vm: PostsViewModel = viewModel(
                    factory = PostsViewModelFactory(PostRepositoryRemote(RetrofitClient.api))
                )
                MuroCampusApp(vm)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MuroCampusApp(vm: PostsViewModel) {
    var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
    BackHandler(enabled = idSeleccionado != null) { idSeleccionado = null }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text("MuroCampus") }) }
    ) { innerPadding ->
        if (idSeleccionado == null) {
            PantallaLista(
                vm = vm,
                onPostClick = { idSeleccionado = it },
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            PantallaDetalle(
                vm = vm,
                postId = idSeleccionado!!,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MuroCampusTheme {
        Scaffold { innerPadding ->
            Text(
                text = "MuroCampus",
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
