package com.ronda.avisoscampus.ui

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ronda.avisoscampus.notificacion.Notificador
import com.ronda.avisoscampus.receiver.ModoAvionReceiver
import com.ronda.avisoscampus.viewmodel.AvisosViewModel
import com.ronda.avisoscampus.viewmodel.EstadoPermiso
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(
    viewModel: AvisosViewModel = viewModel()
) {
    val contexto = LocalContext.current
    val estadoPermiso by viewModel.estadoPermiso.collectAsState()
    val faltan by viewModel.faltan.collectAsState()
    val escuchando by viewModel.escuchandoAvion.collectAsState()
    val avisos by viewModel.avisos.collectAsState()

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        viewModel.permisoRespondido(concedido)
    }

    LaunchedEffect(Unit) {
        if (Notificador.tienePermiso(contexto)) {
            viewModel.permisoRespondido(true)
        }
    }

    LaunchedEffect(viewModel, contexto) {
        viewModel.eventos.collect { aviso ->
            Notificador.mostrar(contexto, aviso)
        }
    }

    DisposableEffect(escuchando, contexto, viewModel) {
        if (escuchando) {
            val receptor = ModoAvionReceiver { activo ->
                viewModel.alCambiarModoAvion(activo)
            }
            ContextCompat.registerReceiver(
                contexto,
                receptor,
                IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            onDispose { contexto.unregisterReceiver(receptor) }
        } else {
            onDispose { }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AvisosCampus") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val icono = when (estadoPermiso) {
                        EstadoPermiso.PERMITIDO -> Icons.Default.NotificationsActive
                        EstadoPermiso.DENEGADO -> Icons.Default.NotificationsOff
                        EstadoPermiso.SIN_PEDIR -> Icons.Default.Notifications
                    }

                    Icon(
                        imageVector = icono,
                        contentDescription = "Notificaciones",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "Permiso de notificaciones",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "Estado: ${estadoPermiso.etiqueta}",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Button(
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                viewModel.permisoRespondido(true)
                            }
                        }
                    ) {
                        Text("Permitir notificaciones")
                    }
                }
            }

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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Avisos",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Button(onClick = { viewModel.avisarAhora() }) {
                        Text("Mostrar aviso")
                    }
                }
            }

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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Recordatorio",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Button(
                        onClick = { viewModel.recordarEn() },
                        enabled = faltan == null
                    ) {
                        Text("Recordar en 10 s")
                    }
                    Button(
                        onClick = { viewModel.cancelarRecordatorio() },
                        enabled = faltan != null
                    ) {
                        Text("Cancelar")
                    }
                    faltan?.let { segundos ->
                        Text("Faltan $segundos s")
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Escuchar el modo avión",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium
                )
                Switch(
                    checked = escuchando,
                    onCheckedChange = viewModel::cambiarEscucha
                )
            }

            HistorialAvisos(avisos)
        }
    }
}
