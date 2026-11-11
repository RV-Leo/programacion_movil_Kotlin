package com.tecsup.computerstore.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tecsup.computerstore.domain.model.Item

@Composable
fun ItemImage(item: Item, modifier: Modifier = Modifier, contentScale: ContentScale = ContentScale.Crop) {
    val context = LocalContext.current
    val localDrawableId = remember(item.id) {
        context.resources.getIdentifier("product_${item.id}", "drawable", context.packageName)
    }
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        when {
            localDrawableId != 0 -> Image(
                painter = painterResource(localDrawableId),
                contentDescription = item.nombre,
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize()
            )
            item.imagenUrl.isNotBlank() -> AsyncImage(
                model = item.imagenUrl,
                contentDescription = item.nombre,
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize()
            )
            else -> {
            val codigo = when (item.categoria) {
                "Placas madre" -> "MB"
                "Procesadores" -> "CPU"
                "Memoria RAM" -> "RAM"
                "Almacenamiento" -> "SSD"
                "Tarjetas gráficas" -> "GPU"
                "Fuentes de poder" -> "PSU"
                "Refrigeración" -> "FAN"
                "Gabinetes" -> "PC"
                "Teclados" -> "KEY"
                "Mouse" -> "MOUSE"
                "USB hubs" -> "USB"
                "Monitores portátiles" -> "MON"
                "Micrófonos" -> "MIC"
                "Webcams" -> "CAM"
                "Audífonos" -> "AUDIO"
                else -> "TECH"
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = CircleShape) {
                    Text(codigo, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp))
                }
                Text(item.categoria.uppercase(), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 10.dp))
            }
            }
        }
    }
}
