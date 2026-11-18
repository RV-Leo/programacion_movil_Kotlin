package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.ui.components.ProductoCard
import com.tecsup.cafecampus.viewmodel.MenuViewModel

private val FondoMenu = Color(0xFFFFFBF6)
private val NaranjaCafe = Color(0xFFEB5A0A)

@Composable
fun MenuScreen(vm: MenuViewModel, recibirPedidos: Boolean, onProducto: (Int) -> Unit, onAbrirCarrito: () -> Unit, onAjustes: () -> Unit, onMisPedidos: () -> Unit, onCerrarSesion: () -> Unit) {
    val productos by vm.productos.collectAsState()
    val cantidadCarrito by vm.cantidadEnCarrito.collectAsState()
    var busqueda by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Todo") }
    val categorias = listOf("Todo", "Desayunos", "Almuerzos", "Bebidas")
    val productosVisibles = productos.filter { producto ->
        val coincideTexto = producto.nombre.contains(busqueda.trim(), ignoreCase = true)
        val coincideCategoria = categoriaSeleccionada == "Todo" || when (categoriaSeleccionada) {
            "Bebidas" -> producto.categoria.equals("Bebidas", ignoreCase = true)
            "Desayunos" -> producto.categoria.equals("Comidas", ignoreCase = true)
            "Almuerzos" -> producto.categoria.equals("Comidas", ignoreCase = true)
            else -> true
        }
        coincideTexto && coincideCategoria
    }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(horizontal = 18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("¡Hola!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("¿Qué se te antoja hoy?", color = Color(0xFF858585), style = MaterialTheme.typography.bodyMedium)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onAjustes) {
                    Icon(Icons.Default.Settings, contentDescription = "Ajustes", tint = Color(0xFF30343B))
                }
                BadgedBox(badge = { if (cantidadCarrito > 0) Badge { Text(cantidadCarrito.toString()) } }) {
                    IconButton(onClick = onAbrirCarrito) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = "Bolsa, $cantidadCarrito productos", tint = Color(0xFF30343B))
                    }
                }
                IconButton(onClick = onCerrarSesion) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión y cambiar perfil", tint = NaranjaCafe)
                }
            }
        }

        if (!recibirPedidos) {
            androidx.compose.material3.Card(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFFFFE4E4))
            ) {
                Text("Cafetería cerrada · No se pueden confirmar pedidos", modifier = Modifier.padding(14.dp), color = Color(0xFFB3261E), fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(Modifier.height(14.dp))
        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar en el menú…", color = Color(0xFF969696)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF858585)) },
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedBorderColor = Color(0xFFE6E2DE),
                focusedBorderColor = NaranjaCafe
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            categorias.forEach { categoria ->
                FilterChip(
                    selected = categoriaSeleccionada == categoria,
                    onClick = { categoriaSeleccionada = categoria },
                    label = { Text(categoria) },
                    shape = RoundedCornerShape(50.dp),
                    colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NaranjaCafe,
                        selectedLabelColor = Color.White,
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        }

        Text(
            "Populares hoy",
            modifier = Modifier.padding(top = 13.dp, bottom = 9.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(productosVisibles, key = { it.id }) { producto ->
                ProductoCard(
                    producto = producto,
                    onClick = { onProducto(producto.id) },
                    onAgregar = { vm.agregar(producto) }
                )
            }
        }
    }
}






