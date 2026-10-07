package com.ronda.tienda_tecsup

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Badge
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch

private data class DestinoDrawer(
    val nombre: String,
    val ruta: String,
    val icono: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route
    val rutaSeleccionada = if (rutaActual == "detalle/{productoId}") "inicio" else rutaActual
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var favoritos by remember { mutableStateOf(emptySet<Int>()) }

    val destinosDrawer = listOf(
        DestinoDrawer("Inicio", "inicio", Icons.Default.Home),
        DestinoDrawer("Mis pedidos", "mis_pedidos", Icons.Default.Receipt),
        DestinoDrawer("Favoritos", "favoritos", Icons.Default.Favorite),
        DestinoDrawer("Perfil", "perfil", Icons.Default.Person),
        DestinoDrawer("Cerrar sesión", "cerrar_sesion", Icons.AutoMirrored.Filled.ExitToApp)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ) {
                            Text(
                                text = "LR",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text("Leonardo Ronda", style = MaterialTheme.typography.titleSmall)
                            Text(
                                "leonardo.ronda@tecsup.edu.pe",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(modifier = Modifier.height(12.dp))
                destinosDrawer.forEach { destino ->
                    NavigationDrawerItem(
                        label = { Text(destino.nombre) },
                        icon = { Icon(destino.icono, contentDescription = null) },
                        badge = {
                            if (destino.ruta == "favoritos") {
                                Badge { Text(favoritos.size.toString()) }
                            }
                        },
                        selected = rutaSeleccionada == destino.ruta,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                if (destino.ruta == "cerrar_sesion") {
                                    Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                                } else {
                                    navController.navigate(destino.ruta) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
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
                    title = {
                        Column {
                            Text(
                                text = "TECSUP Store",
                                style = MaterialTheme.typography.titleLarge
                            )

                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Abrir menú",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
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
                composable("mis_pedidos") {
                    PantallaSeccion(
                        titulo = "Mis pedidos",
                        mensaje = "Todavía no tienes pedidos."
                    )
                }
                composable("favoritos") {
                    PantallaFavoritos(
                        favoritoIds = favoritos,
                        onProductoClick = { producto ->
                            navController.navigate("detalle/${producto.id}")
                        },
                        onCambiarFavorito = { producto ->
                            favoritos = favoritos - producto.id
                        }
                    )
                }
                composable("perfil") {
                    PantallaSeccion(
                        titulo = "Perfil",
                        mensaje = "Perfil de usuario TECSUP"
                    )
                }
            }
        }
    }
}
