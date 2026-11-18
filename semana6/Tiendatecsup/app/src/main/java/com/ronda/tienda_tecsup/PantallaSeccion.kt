package com.ronda.tienda_tecsup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaSeccion(titulo: String, mensaje: String) {
    Column(modifier = Modifier.padding(24.dp)) {
        Text(titulo, style = MaterialTheme.typography.titleLarge)
        Text(
            text = mensaje,
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
