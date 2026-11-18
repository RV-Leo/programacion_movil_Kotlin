package com.ronda.tarealab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ronda.tarealab3.ui.theme.TareaLab3Theme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextAlign

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TareaLab3Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    PantallaRegistroNotas(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PantallaRegistroNotas(modifier: Modifier = Modifier) {

    //VARIABLES
    var notaFunProg by remember { mutableFloatStateOf(0f) }
    var notaPoo by remember { mutableFloatStateOf(0f) }
    var notaMovil by remember { mutableFloatStateOf(0f) }
    var notaBd by remember { mutableFloatStateOf(0f) }

    var redondear by remember { mutableStateOf(false) }
    var confirmado by remember { mutableStateOf(false) }
    var calculado by remember { mutableStateOf(false) }

    val colorsDegradado = listOf(
        Color(0xFF7F00FD),
        Color(0xFF43007C)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFB983F6),
                        Color(0xFFE5DDFC)
                    )
                )
            )
            .verticalScroll(rememberScrollState())
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = colorsDegradado
                    )
                )
                .padding(
                    vertical = 16.dp,
                    horizontal = 20.dp
                )
        ) {
            Text(
                text = "Registro de Notas",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Notas del ciclo",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                text = "Desliza para asignar cada nota (0 a 20)",
                fontSize = 12.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // CURSOS
            Curso(
                nombre = "Fundamentos de Programación",
                peso = 20,
                nota = notaFunProg
            ) {
                notaFunProg = it
                calculado = false
            }

            Curso(
                nombre = "Programación Orientada a Objetos",
                peso = 25,
                nota = notaPoo
            ) {
                notaPoo = it
                calculado = false
            }

            Curso(
                nombre = "Programación en Móviles",
                peso = 30,
                nota = notaMovil
            ) {
                notaMovil = it
                calculado = false
            }

            Curso(
                nombre = "Base de Datos",
                peso = 25,
                nota = notaBd
            ) {
                notaBd = it
                calculado = false
            }

            Spacer(modifier = Modifier.height(12.dp))

            // SWITCH
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Redondear promedio final",
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )

                Switch(
                    checked = redondear,
                    onCheckedChange = {
                        redondear = it
                        calculado = false
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF7F00FD),
                        checkedTrackColor = Color(0xFFE0C7FF),
                        uncheckedThumbColor = Color(0xFF2D2D2D),
                        uncheckedTrackColor = Color.Transparent
                    )
                )
            }

            // CHECKBOX
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Checkbox(
                    checked = confirmado,
                    onCheckedChange = {
                        confirmado = it

                        if (!it) {
                            calculado = false
                        }
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF7F00FD),
                        uncheckedColor = Color(0xFF4B4B4B),
                        checkmarkColor = Color.White
                    )
                )

                Text(
                    text = "Confirmo que las notas son correctas",
                    fontSize = 13.sp
                )
            }

            // MENSAJE ANTES DE CALCULAR
            if (!calculado) {
                Text(
                    text = "Asigna las notas y confirma para calcular",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(
                        vertical = 8.dp
                    )
                )
            }

            // BOTONES
            Button(
                onClick = {
                    calculado = true
                },
                enabled = confirmado,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("CALCULAR PROMEDIO")
            }
            Button(
                onClick = {
                    notaFunProg = 0f
                    notaPoo = 0f
                    notaMovil = 0f
                    notaBd = 0f

                    redondear = false
                    confirmado = false
                    calculado = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("LIMPIAR")
            }


            if (calculado) {

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                val promedioCalculado = kotlin.math.round(
                    ((notaFunProg * 0.20f) +
                                    (notaPoo * 0.25f) +
                                    (notaMovil * 0.30f) +
                                    (notaBd * 0.25f)) * 100) / 100

                val promedioFinal =
                    if (redondear) {
                        kotlin.math.round(promedioCalculado)
                    } else {
                        promedioCalculado
                    }
                val estado = when {
                    promedioFinal >= 17 -> "EXCELENTE"
                    promedioFinal >= 13 -> "APROBADO"
                    promedioFinal >= 10 -> "EN RECUPERACIÓN"
                    else -> "DESAPROBADO"
                }
                val colorestado = when {
                    promedioFinal >= 17 -> Color(0xFFA5D6A7)
                    promedioFinal >= 13 -> Color(0xFFC8E6C9)
                    promedioFinal >= 10 -> Color(0xFFFFD54F)
                    else -> Color(0xFFEF9A9A)
                }
                val colorletraestado = when {
                    promedioFinal >= 17 -> Color(0xFF0D4F1C)
                    promedioFinal >= 13 -> Color(0xFF087F23)
                    promedioFinal >= 10 -> Color(0xFFB26A00)
                    else -> Color(0xFFB71C1C)
                }

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = "Resultados",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5800D2)
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Text(
                            text = "Fundamentos de Programación: \n${notaFunProg.toInt()} × 20% = ${
                                kotlin.math.round(notaFunProg * 0.20f * 100) / 100
                            }",
                            color = Color.DarkGray,
                        )

                        Text(
                            text = "Programación Orientada a Objetos: \n${notaPoo.toInt()} × 25% = ${
                                kotlin.math.round(notaPoo * 0.25f * 100) / 100
                            }",
                            color = Color.DarkGray,
                        )

                        Text(
                            text = "Programación en Móviles: \n${notaMovil.toInt()} × 30% = ${
                                kotlin.math.round(notaMovil * 0.30f * 100) / 100 
                            }",
                            color = Color.DarkGray,
                        )

                        Text(
                            text = "Base de Datos: \n${notaBd.toInt()} × 25% = ${
                                kotlin.math.round(notaBd * 0.25f * 100) / 100
                            }",
                            color = Color.DarkGray,
                        )
                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Text(
                            text = "Promedio ponderado: ${promedioCalculado}",
                            color = Color.DarkGray,
                        )

                        Text(
                            text = "Promedio final: ${"%.2f".format(promedioFinal)}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5800D2)
                        )

                        if (redondear) {
                            Text(
                                text = "Promedio redondeado",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(
                                    color = colorestado,
                                    shape = RoundedCornerShape(50)
                                )
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                )
                        ) {
                            Text(
                                text = estado,
                                color = colorletraestado,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        }
                    }
                Text(
                    text = " ✅ Promedio calculado correctamente",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00BE2A),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Desarrollado por Leonardo Favio Ronda Vidalon",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                }

            }
        }
    }

@Composable
fun Curso(
    nombre: String,
    peso: Int,
    nota: Float,
    onNotaChange: (Float) -> Unit
) {

    Column(
        modifier = Modifier.padding(
            vertical = 4.dp
        )
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "$nombre ($peso%)",
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp
            )
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0x81E1BCFF).copy(alpha = 0.6f),
                        shape = RoundedCornerShape(50)
                    )
                    .padding(
                        horizontal = 8.dp,
                        vertical = 3.dp
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${nota.toInt()}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (nota < 13) {
                        Color(0xFFC40000)
                    } else {
                        Color(0xFF008F1B)
                    }
                )
            }

        }
        //SLIDER
        Slider(
            value = nota,
            onValueChange = {
                onNotaChange(it.toInt().toFloat())
            },
            valueRange = 0f..20f,
            steps = 19,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF7F00FD),
                activeTrackColor = Color(0xFF7F00FD),
                inactiveTrackColor = Color(0xFFD0B8F5)
            )
        )
    }
}
