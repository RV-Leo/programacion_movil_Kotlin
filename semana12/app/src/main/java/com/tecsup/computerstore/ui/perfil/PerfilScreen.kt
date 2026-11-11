package com.tecsup.computerstore.ui.perfil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PerfilScreen(
    nombre: String,
    correo: String,
    uid: String,
    esAdmin: Boolean,
    modoOscuro: Boolean,
    onModoOscuroChange: (Boolean) -> Unit,
    onFavoritesClick: () -> Unit,
    onAdminClick: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text("Mi perfil", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(22.dp)) {
                Column(Modifier.fillMaxWidth().padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = CircleShape) {
                            Text(
                                text = nombre.firstOrNull()?.uppercase() ?: "U",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)
                            )
                        }
                        Column(Modifier.weight(1f).padding(start = 14.dp)) {
                            Text(nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text(if (esAdmin) "Administrador" else "Cliente", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    HorizontalDivider()
                    ProfileInfoRow(Icons.Default.MailOutline, "Correo electrónico", correo)
                    ProfileInfoRow(Icons.Default.Shield, "Tipo de cuenta", if (esAdmin) "Administrador" else "Cliente")
                    ProfileInfoRow(Icons.Default.Person, "Identificador de cuenta", uid)
                }
            }
            Text("Accesos rápidos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(18.dp)) {
                Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DarkMode, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Column(Modifier.weight(1f).padding(start = 12.dp)) {
                        Text("Modo oscuro", fontWeight = FontWeight.SemiBold)
                        Text("Ajusta la apariencia de la tienda", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(checked = modoOscuro, onCheckedChange = onModoOscuroChange)
                }
            }
            OutlinedButton(onClick = onFavoritesClick, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.FavoriteBorder, contentDescription = null)
                Text("  Mis favoritos")
            }
            if (esAdmin) {
                OutlinedButton(onClick = onAdminClick, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null)
                    Text("  Panel de administración")
                }
            }
            Spacer(Modifier.height(8.dp))
            Text("ComputerStore", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Text("Tu espacio para encontrar equipos de cómputo.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(4.dp))
            Button(onClick = onLogout, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                Icon(Icons.Default.Logout, contentDescription = null)
                Text("  Cerrar sesión")
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun ProfileInfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(top = 16.dp), verticalAlignment = Alignment.Top) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 2.dp))
        Column(Modifier.padding(start = 12.dp)) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value.ifBlank { "No disponible" }, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
        }
    }
}
