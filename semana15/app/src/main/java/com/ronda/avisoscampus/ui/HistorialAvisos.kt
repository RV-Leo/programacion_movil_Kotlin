package com.ronda.avisoscampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ronda.avisoscampus.model.Aviso
import java.text.DateFormat
import java.util.Date

@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    val formatoHora = DateFormat.getTimeInstance(DateFormat.SHORT)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Historial",
                style = MaterialTheme.typography.titleMedium
            )

            val recientes = avisos.take(20)
            if (recientes.isEmpty()) {
                Text("Sin avisos todavía")
            } else {
                recientes.forEach { aviso ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${aviso.origen.texto} · ${formatoHora.format(Date(aviso.hora))}",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = aviso.titulo,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = aviso.texto,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
