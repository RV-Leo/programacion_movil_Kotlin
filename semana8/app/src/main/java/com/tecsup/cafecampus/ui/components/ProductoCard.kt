package com.tecsup.cafecampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.data.local.entity.Producto
import java.util.Locale

private val NaranjaCafe = Color(0xFFEB5A0A)

@Composable
fun ProductoCard(
    producto: Producto,
    onClick: () -> Unit = {},
    onAgregar: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = painterResource(id = imagenProducto(producto.nombre)),
                contentDescription = "Imagen de ${producto.nombre}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(96.dp).clip(RoundedCornerShape(14.dp))
            )
            Text(
                text = producto.nombre,
                modifier = Modifier.padding(top = 9.dp, start = 3.dp, end = 3.dp),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 3.dp, top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "S/ ${"%.2f".format(Locale.US, producto.precio)}",
                    color = NaranjaCafe,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = onAgregar,
                    enabled = producto.disponible,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (producto.disponible) NaranjaCafe else Color.LightGray)
                        .padding(1.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar ${producto.nombre}", tint = Color.White)
                }
            }
        }
    }
}
