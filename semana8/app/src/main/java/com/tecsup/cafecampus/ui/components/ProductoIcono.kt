package com.tecsup.cafecampus.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.data.local.entity.Producto

@Composable
fun ProductoIcono(producto: Producto, size: Dp = 56.dp) {
    Image(
        painter = painterResource(imagenProducto(producto.nombre)),
        contentDescription = "Imagen de ${producto.nombre}",
        contentScale = ContentScale.Crop,
        modifier = Modifier.size(size).clip(RoundedCornerShape(12.dp))
    )
}
