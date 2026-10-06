package com.ronda.tienda_tecsup

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var favoritos by remember { mutableStateOf(emptySet<Int>()) }
    val destinosDrawer = listOf("Inicio", "Mis pedidos", "Favoritos", "Perfil")

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Secciones",
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                destinosDrawer.forEach { destino ->
                    NavigationDrawerItem(
                        label = { Text(destino) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("TECSUP Store") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    }
                )
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
}
