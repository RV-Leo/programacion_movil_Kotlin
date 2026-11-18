package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.viewmodel.SesionViewModel

private val MoradoAjustes = Color(0xFF5546E8)

@Composable
fun AjustesScreen(
    vm: SesionViewModel,
    onVolver: () -> Unit,
    onCambiarPerfil: (String) -> Unit,
    onCerrarSesion: () -> Unit
) {
    val perfil by vm.perfilActivo.collectAsState()
    val modoOscuro by vm.modoOscuro.collectAsState()
    val recibirPedidos by vm.recibirPedidos.collectAsState()
    val esAdmin = perfil.equals(Rol.ADMIN.name, ignoreCase = true)

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text("Ajustes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(if (esAdmin) "Administrador" else "Estudiante", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Perfil activo · modo práctica", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Button(
            onClick = { vm.cambiarPerfil(onCambiarPerfil) },
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MoradoAjustes, contentColor = Color.White)
        ) {
            Icon(Icons.Default.SwapHoriz, contentDescription = null)
            Text(if (esAdmin) "Cambiar a estudiante" else "Cambiar a administrador", modifier = Modifier.padding(start = 8.dp), fontWeight = FontWeight.SemiBold)
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(Modifier.padding(horizontal = 16.dp)) {
                FilaAjuste(
                    icon = { Icon(Icons.Default.DarkMode, contentDescription = null, tint = MoradoAjustes) },
                    titulo = "Modo oscuro",
                    descripcion = "Aplicar el tema oscuro en toda la app",
                    checked = modoOscuro,
                    onCheckedChange = vm::guardarModoOscuro
                )
                if (esAdmin) {
                    FilaAjuste(
                        icon = { Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = MoradoAjustes) },
                        titulo = "Recibir pedidos",
                        descripcion = if (recibirPedidos) "La cafetería está abierta" else "Los clientes no pueden confirmar pedidos",
                        checked = recibirPedidos,
                        onCheckedChange = vm::guardarRecibirPedidos
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))
        OutlinedButton(
            onClick = onCerrarSesion,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(14.dp)
        ) { Text("Cerrar sesión", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold) }
    }
}

@Composable
private fun FilaAjuste(
    icon: @Composable () -> Unit,
    titulo: String,
    descripcion: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Text(descripcion, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
