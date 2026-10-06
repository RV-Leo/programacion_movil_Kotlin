package com.ronda.tienda_tecsup

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    var favoritos by remember { mutableStateOf(emptySet<Int>()) }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("TECSUP Store") })
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "inicio",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("inicio") {
                PantallaInicio(
                    onProductoClick = { producto ->
                        navController.navigate("detalle/${producto.id}")
                    },
                    favoritos = favoritos,
                    onCambiarFavorito = { producto ->
                        favoritos = if (producto.id in favoritos) {
                            favoritos - producto.id
                        } else {
                            favoritos + producto.id
                        }
                    }
                )
            }
            composable(
                route = "detalle/{productoId}",
                arguments = listOf(navArgument("productoId") { type = NavType.IntType })
            ) { entrada ->
                val productoId = entrada.arguments?.getInt("productoId")
                val producto = productosEjemplo.firstOrNull { it.id == productoId }
                PantallaDetalleProducto(producto = producto) {
                    if (!navController.popBackStack()) {
                        navController.navigate("inicio")
                    }
                }
            }
        }
    }
}
