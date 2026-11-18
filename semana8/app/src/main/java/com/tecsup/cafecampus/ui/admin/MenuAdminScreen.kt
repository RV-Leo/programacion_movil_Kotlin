package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.ui.components.ProductoIcono
import com.tecsup.cafecampus.viewmodel.MenuAdminViewModel

private val MoradoMenu = Color(0xFF5546E8)
private val FondoMenuAdmin = Color(0xFFF8F7FC)

@Composable
fun MenuAdminScreen(
    vm: MenuAdminViewModel,
    onVolver: () -> Unit,
    onNuevo: () -> Unit,
    onEditar: (Int) -> Unit
) {
    val productos by vm.productos.collectAsState()
    var eliminarProducto by remember { mutableStateOf<Producto?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(onClick = onNuevo, containerColor = MoradoMenu, contentColor = Color.White) {
                Icon(Icons.Default.Add, contentDescription = "Agregar producto")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
                IconButton(onClick = onVolver) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
                Column {
                    Text("Gestión del menú", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("${productos.size} productos", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxSize()) {
                items(productos, key = { it.id }) { producto ->
                    ProductoAdminItem(
                        producto = producto,
                        onEditar = { onEditar(producto.id) },
                        onEliminar = { eliminarProducto = producto },
                        onCambiarDisponibilidad = { vm.alternarDisponibilidad(producto) }
                    )
                }
            }
        }
    }

    eliminarProducto?.let { producto ->
        AlertDialog(
            onDismissRequest = { eliminarProducto = null },
            title = { Text("Eliminar producto") },
            text = { Text("¿Seguro que deseas eliminar ${producto.nombre} del menú?") },
            confirmButton = {
                TextButton(onClick = {
                    vm.eliminar(producto)
                    eliminarProducto = null
                }) { Text("Eliminar", color = Color(0xFFD32F2F)) }
            },
            dismissButton = { TextButton(onClick = { eliminarProducto = null }) { Text("Cancelar") } }
        )
    }
}

@Composable
private fun ProductoAdminItem(
    producto: Producto,
    onEditar: () -> Unit,
    onEliminar: () -> Unit,
    onCambiarDisponibilidad: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductoIcono(producto, size = 60.dp)
            Column(modifier = Modifier.weight(1f).padding(horizontal = 10.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(producto.nombre, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, maxLines = 1)
                Text("${producto.categoria} · S/ ${"%.2f".format(java.util.Locale.US, producto.precio)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                TextButtonDisponibilidad(producto.disponible, onCambiarDisponibilidad)
            }
            IconButton(onClick = onEditar) { Icon(Icons.Default.Edit, contentDescription = "Editar ${producto.nombre}", tint = MoradoMenu) }
            IconButton(onClick = onEliminar) { Icon(Icons.Default.Delete, contentDescription = "Eliminar ${producto.nombre}", tint = Color(0xFFD34242)) }
        }
    }
}

@Composable
private fun TextButtonDisponibilidad(disponible: Boolean, onClick: () -> Unit) {
    AssistChip(
        onClick = onClick,
        label = { Text(if (disponible) "Disponible" else "Agotado") },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (disponible) Color(0xFFE0F6E8) else Color(0xFFFFE6E6),
            labelColor = if (disponible) Color(0xFF218957) else Color(0xFFBC3A3A)
        ),
        border = null
    )
}

