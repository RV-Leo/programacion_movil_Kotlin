package com.tecsup.cafecampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.tecsup.cafecampus.data.local.CafeDatabase
import com.tecsup.cafecampus.data.prefs.PreferenciasStore
import com.tecsup.cafecampus.data.repository.CafeRepositoryRoom
import com.tecsup.cafecampus.ui.navigation.NavCafe
import com.tecsup.cafecampus.ui.theme.CafeCampusTheme
import com.tecsup.cafecampus.viewmodel.CafeViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = CafeDatabase.obtener(applicationContext)
        val prefs = PreferenciasStore(applicationContext)
        val repository = CafeRepositoryRoom(
            productoDao = db.productoDao(),
            usuarioDao = db.usuarioDao(),
            pedidoDao = db.pedidoDao()
        )
        val factory = CafeViewModelFactory(repository, prefs)

        setContent {
            CafeCampusTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavCafe(
                            navController = navController,
                            factory = factory
                        )
                    }
                }
            }
        }
    }
}
