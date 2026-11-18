package com.ronda.campusgps.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ronda.campusgps.model.*
import com.ronda.campusgps.ui.theme.CampusGPSTheme
import com.ronda.campusgps.util.formatearDistancia
import com.ronda.campusgps.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa() {
    val context = LocalContext.current
    val vm: MapaViewModel = viewModel(factory = CampusViewModelFactory(context.applicationContext))

    val estado by vm.estado.collectAsState()
    val lugares by vm.lugares.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        if (result.values.any { it }) {
            vm.buscar()
        } else {
            vm.permisoDenegado()
        }
    }

    fun buscar() {
        val fine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fine || coarse) {
            vm.buscar()
        } else {
            launcher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CampusGPS") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionado = seleccionado,
                masCercano = masCercano,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            Button(
                onClick = { buscar() },
                enabled = estado !is UbicacionUiState.Buscando,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(if (estado is UbicacionUiState.Buscando) "Buscando…" else "Obtener mi ubicación")
            }

            OutlinedButton(
                onClick = { masCercano?.let { vm.seleccionar(it.id) } },
                enabled = masCercano != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Ir al más cercano")
            }

            if (masCercano != null) {
                val distanciaTexto = lugares.firstOrNull { it.lugar.id == masCercano!!.id }
                    ?.metros
                    ?.let(::formatearDistancia) ?: ""
                Text(
                    text = "Más cercano: ${masCercano!!.nombre} ($distanciaTexto)",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            if (estado is UbicacionUiState.Error) {
                MensajeError(
                    error = estado as UbicacionUiState.Error,
                    onRetry = { buscar() },
                    onSettings = {
                        context.startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", context.packageName, null)
                            )
                        )
                    }
                )
            }

            ListaLugares(
                lugares = lugares,
                seleccionado = seleccionado,
                onClick = vm::seleccionar,
                modifier = Modifier
                    .height(220.dp)
                    .fillMaxWidth()
            )
        }
    }
}
