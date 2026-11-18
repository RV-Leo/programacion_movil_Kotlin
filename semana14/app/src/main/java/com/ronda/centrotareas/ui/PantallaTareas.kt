package com.ronda.centrotareas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
    val carga by viewModel.carga.collectAsState()
    val simularError by viewModel.simularError.collectAsState()
    var contador by remember { mutableIntStateOf(0) }
    var hastaTexto by remember { mutableStateOf("500000") }
    val hasta = hastaTexto.toIntOrNull()?.takeIf { it >= 2 }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Title Header
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                text = "Centro de Tareas",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Gestión de procesos y corrutinas en Android",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Tarea con progreso Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Tarea con progreso",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                GradientButton(
                    onClick = { contador++ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Tócame: $contador")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        GradientButton(
                            onClick = viewModel::tareaLarga,
                            enabled = !corriendo,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Iniciar")
                        }
                    }
                    OutlinedButton(
                        onClick = viewModel::cancelar,
                        enabled = corriendo,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                }

                LinearProgressIndicator(
                    progress = { progreso },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${(progreso * 100).toInt()} %",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = mensaje,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Contar números primos Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Contar números primos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                OutlinedTextField(
                    value = hastaTexto,
                    onValueChange = { if (it.all(Char::isDigit)) hastaTexto = it },
                    label = { Text("Hasta el número") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        GradientButton(
                            onClick = { hasta?.let(viewModel::contar) },
                            enabled = hasta != null && primos != PrimosUi.Calculando,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Contar")
                        }
                    }
                    OutlinedButton(
                        onClick = viewModel::cancelarPrimos,
                        enabled = primos == PrimosUi.Calculando,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                }

                when (val estado = primos) {
                    is PrimosUi.Listo -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Primos hasta ${hasta ?: "—"}: ${estado.cantidad}", fontWeight = FontWeight.Medium)
                                Text("Tiempo: ${estado.milisegundos} ms", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                    PrimosUi.Calculando -> {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(modifier = Modifier.padding(4.dp))
                            Text("Calculando primos…", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    PrimosUi.Cancelado -> Text("Cálculo cancelado", color = MaterialTheme.colorScheme.error)
                    PrimosUi.Inicial -> Unit
                }
            }
        }

        // Dos cargas Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Dos cargas concurrentes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Simular error de red")
                    Switch(
                        checked = simularError,
                        onCheckedChange = viewModel::cambiarSimularError
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = viewModel::cargarUnaPorUna,
                        enabled = carga != CargaUi.Cargando,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Una por una")
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        GradientButton(
                            onClick = viewModel::cargarALaVez,
                            enabled = carga != CargaUi.Cargando,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("A la vez")
                        }
                    }
                }

                when (val estado = carga) {
                    CargaUi.Cargando -> {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(modifier = Modifier.padding(4.dp))
                            Text("Cargando datos…")
                        }
                    }
                    is CargaUi.Exito -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Modo: ${estado.modo}", fontWeight = FontWeight.Bold)
                                Text("Perfil: ${estado.perfil}")
                                Text("Pedidos: ${estado.pedidos}")
                                Text("Tiempo total: ${estado.milisegundos} ms", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                    is CargaUi.Error -> {
                        Text("Error: ${estado.mensaje}", color = MaterialTheme.colorScheme.error)
                    }
                    else -> Unit
                }
                
                Text(
                    text = "«Una por una» tarda ≈ 5000 ms",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun GradientButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val gradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFFFF7A00), // Orange
            Color(0xFF9C27B0)  // Purple
        )
    )
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        ),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .then(
                    if (enabled) {
                        Modifier.background(gradient)
                    } else {
                        Modifier.background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                    }
                )
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(
                LocalContentColor provides if (enabled) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            ) {
                content()
            }
        }
    }
}
