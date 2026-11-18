package com.ronda.centrotareas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ronda.centrotareas.model.CargaUi
import com.ronda.centrotareas.model.PrimosUi
import com.ronda.centrotareas.viewmodel.TareasViewModel

@Composable
fun PantallaTareas(viewModel: TareasViewModel) {
    val mensaje by viewModel.mensaje.collectAsState()
    val progreso by viewModel.progreso.collectAsState()
    val corriendo by viewModel.corriendo.collectAsState()
    val primos by viewModel.primos.collectAsState()
    val progresoPrimos by viewModel.progresoPrimos.collectAsState()
    val carga by viewModel.carga.collectAsState()
    val simularError by viewModel.simularError.collectAsState()
    var contador by remember { mutableIntStateOf(0) }
    var hastaTexto by remember { mutableStateOf("500000") }
    val hasta = hastaTexto.toIntOrNull()?.takeIf { it >= 2 }

    Column(Modifier.fillMaxSize()) {
        Surface(color = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary) {
            Text("CentroTareas", Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp), fontWeight = FontWeight.Bold)
        }
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Tarea con progreso", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                    Button(onClick = { contador++ }, modifier = Modifier.fillMaxWidth()) { Text("Tócame: $contador") }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = viewModel::tareaLarga, enabled = !corriendo) { Text("Iniciar") }
                        OutlinedButton(onClick = viewModel::cancelar, enabled = corriendo) { Text("Cancelar") }
                    }
                    LinearProgressIndicator(progress = { progreso }, modifier = Modifier.fillMaxWidth())
                    Text("${(progreso * 100).toInt()} %", modifier = Modifier.fillMaxWidth())
                    Text(mensaje, modifier = Modifier.fillMaxWidth())
                }
            }

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Contar números primos", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(
                        value = hastaTexto,
                        onValueChange = { if (it.all(Char::isDigit)) hastaTexto = it },
                        label = { Text("Hasta el número") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { hasta?.let(viewModel::contarConProgreso) }, enabled = hasta != null && primos != PrimosUi.Calculando) { Text("Contar") }
                        OutlinedButton(onClick = viewModel::cancelarPrimos, enabled = primos == PrimosUi.Calculando) { Text("Cancelar") }
                    }
                    if (primos == PrimosUi.Calculando) {
                        LinearProgressIndicator(progress = { progresoPrimos }, modifier = Modifier.fillMaxWidth())
                        Text("${(progresoPrimos * 100).toInt()} %")
                    }
                    when (val estado = primos) {
                        is PrimosUi.Listo -> Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))) {
                            Text("Primos hasta ${hasta ?: "—"}: ${estado.cantidad}\nTiempo: ${estado.milisegundos} ms", Modifier.padding(12.dp))
                        }
                        PrimosUi.Calculando -> Text("Calculando…")
                        PrimosUi.Cancelado -> Text("Cancelado", color = MaterialTheme.colorScheme.error)
                        PrimosUi.Inicial -> Unit
                    }
                }
            }

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Dos cargas", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Simular error")
                        Switch(checked = simularError, onCheckedChange = viewModel::cambiarSimularError)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = viewModel::cargarUnaPorUna, enabled = carga != CargaUi.Cargando) { Text("Una por una") }
                        Button(onClick = viewModel::cargarALaVez, enabled = carga != CargaUi.Cargando) { Text("A la vez") }
                    }
                    when (val estado = carga) {
                        CargaUi.Cargando -> CircularProgressIndicator()
                        is CargaUi.Exito -> Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))) {
                            Text("Modo: ${estado.modo}\nPerfil: ${estado.perfil}\nPedidos: ${estado.pedidos}\nTiempo: ${estado.milisegundos} ms", Modifier.padding(12.dp))
                        }
                        is CargaUi.Error -> Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(estado.mensaje, color = MaterialTheme.colorScheme.onErrorContainer)
                                Button(onClick = viewModel::cargarALaVez) { Text("Reintentar") }
                            }
                        }
                        CargaUi.Inicial -> Unit
                    }
                    Text("«Una por una» tarda ≈ 5000 ms")
                }
            }
            Spacer(Modifier.height(4.dp))
        }
    }
}
