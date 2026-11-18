package com.tecsup.mibodega.ui.cliente.screens.perfil

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
 * Muestra los datos utilizados al crear la cuenta (nombre, teléfono, dirección, referencia)
 * y acceso a "Mis pedidos".
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    nombre: String,
    telefono: String,
    direccion: String,
    referencia: String,
    modoOscuro: Boolean,
    onModoOscuroChanged: (Boolean) -> Unit,
    onVerMisPedidos: () -> Unit,
    onVerFavoritos: () -> Unit,
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

            Spacer(Modifier.height(20.dp))

            //Aqui se hace el cambio de tema de la app
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Modo oscuro", fontWeight = FontWeight.SemiBold)
                    Switch(
                        checked = modoOscuro,
                        onCheckedChange = onModoOscuroChanged
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

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

            Spacer(Modifier.height(16.dp))

            OutlinedButton(
                onClick = onVerMisPedidos,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = VerdeBodega)
                Spacer(Modifier.size(8.dp))
                Text("Ver Mis Pedidos", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = onVerFavoritos,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = VerdeBodega)
                Spacer(Modifier.size(8.dp))
                Text("Mis Favoritos", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
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
            nombre = "Leonardo Ronda",
            telefono = "987654321",
            direccion = "Av. Los cerezos 123",
            referencia = "Frente al parque",
            modoOscuro = false,
            onModoOscuroChanged = {},
            onVerMisPedidos = {},
            onVerFavoritos = {},
            onCerrarSesion = {}
        )
    }
}
