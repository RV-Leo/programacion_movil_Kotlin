package com.tecsup.mibodega.ui.cliente.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonSecundario
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Pantalla de Perfil del cliente.
 * Muestra los datos utilizados al crear la cuenta (nombre, teléfono, dirección, referencia).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    nombre: String,
    telefono: String,
    direccion: String,
    referencia: String,
    onCerrarSesion: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", fontWeight = FontWeight.Bold) }
            )
        }
    ) { paddingInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(GrisClaro, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto de perfil",
                    tint = VerdeBodega,
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = nombre.ifEmpty { "Usuario" },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    ItemDatoPerfil(titulo = "Teléfono", valor = telefono.ifEmpty { "No registrado" })
                    Spacer(Modifier.height(16.dp))
                    ItemDatoPerfil(titulo = "Dirección de entrega", valor = direccion.ifEmpty { "No registrada" })
                    Spacer(Modifier.height(16.dp))
                    ItemDatoPerfil(titulo = "Referencia", valor = referencia.ifEmpty { "Ninguna" })
                }
            }

            Spacer(Modifier.weight(1f))

            BotonSecundario(
                texto = "Cerrar sesión",
                onClick = onCerrarSesion
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ItemDatoPerfil(titulo: String, valor: String) {
    Column {
        Text(
            text = titulo,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PerfilPreview() {
    BodegaTheme {
        PerfilScreen(
            nombre = "Juan Pérez",
            telefono = "987654321",
            direccion = "Av. Los Olivos 123",
            referencia = "Frente al parque",
            onCerrarSesion = {}
        )
    }
}
