package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.ui.components.CafeButton
import androidx.compose.ui.res.painterResource
import com.tecsup.cafecampus.ui.components.imagenProducto
import com.tecsup.cafecampus.viewmodel.MenuViewModel
import java.util.Locale

private val FondoDetalle = Color(0xFFFFFBF6)
private val NaranjaDetalle = Color(0xFFEB5A0A)
private val Tinta = Color(0xFF30343B)

@Composable
fun DetalleProductoScreen(
    vm: MenuViewModel,
    productoId: Int,
    onVolver: () -> Unit,
    onAgregado: () -> Unit
) {
    val productos by vm.productos.collectAsState()
    val producto = productos.firstOrNull { it.id == productoId }
    var cantidad by remember(productoId) { mutableIntStateOf(1) }
    var nota by remember(productoId) { mutableStateOf("") }

    if (producto == null) {
        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.material3.CircularProgressIndicator(color = NaranjaDetalle)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(42.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver, modifier = Modifier.size(42.dp)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Tinta)
            }
            Text(
                "Detalle del producto",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(Modifier.size(42.dp))
        }

        Image(
            painter = painterResource(id = imagenProducto(producto.nombre)),
            contentDescription = "Imagen de ${producto.nombre}",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(174.dp).clip(RoundedCornerShape(22.dp))
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(producto.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Text(
                "S/ ${"%.2f".format(Locale.US, producto.precio)}",
                style = MaterialTheme.typography.titleMedium,
                color = NaranjaDetalle,
                fontWeight = FontWeight.Bold
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AssistChip(
                onClick = {},
                label = { Text(if (producto.disponible) "Disponible" else "Agotado") },
                leadingIcon = {
                    Box(Modifier.size(7.dp).clip(CircleShape).background(if (producto.disponible) Color(0xFF25A66A) else Color.Gray))
                },
                colors = androidx.compose.material3.AssistChipDefaults.assistChipColors(
                    containerColor = if (producto.disponible) Color(0xFFE0F6E8) else Color(0xFFE9E9E9),
                    labelColor = if (producto.disponible) Color(0xFF218957) else Color.DarkGray
                ),
                border = null
            )
            Text("◷  10–15 min", style = MaterialTheme.typography.bodySmall, color = Color(0xFF858585))
        }

        Text(producto.descripcion, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF747474))
        Text("Cantidad", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.clip(RoundedCornerShape(50.dp)).background(MaterialTheme.colorScheme.surface).padding(horizontal = 5.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { if (cantidad > 1) cantidad-- }, enabled = cantidad > 1, modifier = Modifier.size(38.dp)) {
                    Icon(Icons.Default.Remove, contentDescription = "Restar", tint = NaranjaDetalle)
                }
                Text(cantidad.toString(), modifier = Modifier.padding(horizontal = 14.dp), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                IconButton(onClick = { cantidad++ }, enabled = producto.disponible, modifier = Modifier.size(38.dp)) {
                    Icon(Icons.Default.Add, contentDescription = "Sumar", tint = NaranjaDetalle)
                }
            }
        }

        Text("Nota para la cocina", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        OutlinedTextField(
            value = nota,
            onValueChange = { nota = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Sin ají, por favor", color = Color(0xFF8B8B8B)) },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            minLines = 2,
            maxLines = 3,
            shape = RoundedCornerShape(14.dp),
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedBorderColor = Color(0xFFE7E3DF),
                focusedBorderColor = NaranjaDetalle
            )
        )

        Column(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color(0xFFFFF3E8)).padding(14.dp)
        ) {
            Text("Total", style = MaterialTheme.typography.labelMedium, color = Color(0xFF858585))
            Text(
                "S/ ${"%.2f".format(Locale.US, producto.precio * cantidad)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = NaranjaDetalle
            )
        }

        CafeButton(
            text = if (producto.disponible) "Agregar al pedido" else "Agotado",
            onClick = {
                vm.agregar(producto, cantidad, nota)
                onAgregado()
            },
            containerColor = NaranjaDetalle,
            enabled = producto.disponible
        )
        Spacer(Modifier.height(8.dp))
    }
}
