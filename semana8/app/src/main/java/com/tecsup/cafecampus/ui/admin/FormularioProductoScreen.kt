package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.ui.components.ProductoIcono
import com.tecsup.cafecampus.viewmodel.MenuAdminViewModel

private val MoradoFormulario = Color(0xFF5546E8)
private val FondoFormulario = Color(0xFFF8F7FC)

@Composable
fun FormularioProductoScreen(
    vm: MenuAdminViewModel,
    productoId: Int? = null,
    onVolver: () -> Unit
) {
    val productos by vm.productos.collectAsState()
    val productoExistente = productos.firstOrNull { it.id == productoId }
    var nombre by remember(productoId) { mutableStateOf("") }
    var categoria by remember(productoId) { mutableStateOf("Bebidas") }
    var precio by remember(productoId) { mutableStateOf("") }
    var descripcion by remember(productoId) { mutableStateOf("") }
    var disponible by remember(productoId) { mutableStateOf(true) }
    var error by remember(productoId) { mutableStateOf<String?>(null) }
    var inicializado by remember(productoId) { mutableStateOf(productoId == null) }

    LaunchedEffect(productoId, productoExistente) {
        if (productoExistente != null) {
            nombre = productoExistente.nombre
            categoria = productoExistente.categoria
            precio = productoExistente.precio.toString()
            descripcion = productoExistente.descripcion
            disponible = productoExistente.disponible
            inicializado = true
        }
    }

    if (!inicializado) {
        Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            CircularProgressIndicator(color = MoradoFormulario)
        }
        return
    }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState()).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver") }
            Text(if (productoId == null) "Nuevo producto" else "Editar producto", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it; error = null },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nombre del producto") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )
        OutlinedTextField(
            value = categoria,
            onValueChange = { categoria = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Categoría") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )
        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it; error = null },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Precio (S/)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )
        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Descripción") },
            minLines = 3,
            maxLines = 5,
            shape = RoundedCornerShape(14.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp)).padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text("Disponible en el menú", fontWeight = FontWeight.SemiBold)
                Text("Los estudiantes podrán pedirlo", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(checked = disponible, onCheckedChange = { disponible = it })
        }
        if (error != null) Text(error!!, color = MaterialTheme.colorScheme.error)

        Spacer(Modifier.height(4.dp))
        Button(
            onClick = {
                val precioNumerico = precio.trim().replace(',', '.').toDoubleOrNull()
                error = when {
                    nombre.isBlank() -> "Ingresa el nombre del producto."
                    precioNumerico == null || precioNumerico <= 0 -> "El precio debe ser mayor que 0."
                    else -> null
                }
                if (error == null) {
                    vm.guardar(
                        Producto(
                            id = productoExistente?.id ?: 0,
                            nombre = nombre.trim(),
                            categoria = categoria.trim().ifBlank { "Otros" },
                            precio = precioNumerico!!,
                            descripcion = descripcion.trim(),
                            disponible = disponible,
                        )
                    )
                    onVolver()
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MoradoFormulario)
        ) { Text("Guardar producto", fontWeight = FontWeight.Bold) }
        OutlinedButton(onClick = onVolver, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(12.dp)) {
            Text("Cancelar", color = MoradoFormulario)
        }
    }
}

