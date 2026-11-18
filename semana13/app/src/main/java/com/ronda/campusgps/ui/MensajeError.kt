package com.ronda.campusgps.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ronda.campusgps.model.*

@Composable
fun MensajeError(
    error: UbicacionUiState.Error,
    onRetry: () -> Unit,
    onSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            text = error.mensaje,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

        when (error.tipo) {
            TipoError.SIN_PERMISO -> {
                OutlinedButton(onClick = onSettings) {
                    Text("Abrir ajustes")
                }
            }
            TipoError.GPS_APAGADO -> {
                OutlinedButton(onClick = onRetry) {
                    Text("Reintentar")
                }
            }
            else -> Unit
        }
    }
}
