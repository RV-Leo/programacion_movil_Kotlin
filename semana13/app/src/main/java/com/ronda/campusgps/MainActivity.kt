package com.ronda.campusgps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ronda.campusgps.ui.theme.CampusGPSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusGPSTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UbicacionInicialScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun UbicacionInicialScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var latitud by remember { mutableStateOf("-12.046374") }
    var longitud by remember { mutableStateOf("-77.042793") }
    var estadoUbicacion by remember { mutableStateOf("Ubicación lista") }

    val fusedLocationClient: FusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        latitud = location.latitude.toString()
                        longitud = location.longitude.toString()
                        estadoUbicacion = "Ubicación actualizada"
                    } else {
                        estadoUbicacion = "No se pudo obtener ubicación actual (usando por defecto)"
                    }
                }
            } catch (_: SecurityException) {
                estadoUbicacion = "Error de permisos"
            }
        } else {
            estadoUbicacion = "Permiso denegado"
        }
    }

    fun obtenerUbicacion() {
        val hasFinePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarsePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasFinePermission || hasCoarsePermission) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        latitud = location.latitude.toString()
                        longitud = location.longitude.toString()
                        estadoUbicacion = "Ubicación actualizada"
                    } else {
                        estadoUbicacion = "Obteniendo ubicación..."
                    }
                }
            } catch (_: SecurityException) {
                estadoUbicacion = "Error al leer ubicación"
            }
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "CampusGPS",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Latitud: $latitud",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Longitud: $longitud",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { obtenerUbicacion() }) {
            Text(text = "Obtener Latitud y Longitud ($latitud, $longitud)")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = estadoUbicacion,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UbicacionInicialPreview() {
    CampusGPSTheme {
        UbicacionInicialScreen()
    }
}
