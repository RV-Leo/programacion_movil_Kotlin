package com.ronda.tecsup_fit.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ronda.tecsup_fit.ui.theme.TecsupGreenLight
import com.ronda.tecsup_fit.ui.theme.TecsupGreenPrimary

data class Ejercicio(
    val id: Int,
    val nombre: String,
    val repeticiones: String,
)

data class Rutina(
    val id: Int,
    val nombre: String,
    val nivel: String,
    val ejercicios: List<Ejercicio> = emptyList(),
)

val rutinas: List<Rutina> = listOf(
    Rutina(
        id = 1,
        nombre = "Yoga funcional",
        nivel = "Nivel Principiante",
        ejercicios = listOf(
            Ejercicio(101, "Saludo al sol", "3 series"),
            Ejercicio(102, "Postura del guerrero", "45 seg sostenido"),
            Ejercicio(103, "Postura del árbol", "30 seg por lado"),
        ),
    ),
    Rutina(
        id = 2,
        nombre = "Cross Training",
        nivel = "Nivel Avanzado",
        ejercicios = listOf(
            Ejercicio(201, "Burpees", "15 repeticiones"),
            Ejercicio(202, "Kettlebell swings", "20 repeticiones"),
            Ejercicio(203, "Box jumps", "12 repeticiones"),
            Ejercicio(204, "Flexiones de pecho", "15 repeticiones"),
        ),
    ),
    Rutina(
        id = 3,
        nombre = "Spinning Cardio",
        nivel = "Nivel Intermedio",
        ejercicios = listOf(
            Ejercicio(301, "Sprint de alta velocidad", "1 min"),
            Ejercicio(302, "Subida en montaña con resistencia", "3 min"),
            Ejercicio(303, "Recuperación activa", "2 min"),
        ),
    ),
)

@Composable
fun RoutinesScreen(
    @Suppress("UNUSED_PARAMETER") navController: NavController,
) {
    var expandedRoutineId by remember { mutableStateOf<Int?>(null) }
    var completedExercises by remember { mutableStateOf(setOf<Int>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp),
    ) {
        Text(
            text = "Rutinas interactivas",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Selecciona una rutina para ver los ejercicios y marcar tu progreso",
            fontSize = 13.sp,
            color = Color.Gray,
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            items(rutinas) { rutina ->
                val isExpanded = expandedRoutineId == rutina.id
                val totalEjercicios = rutina.ejercicios.size
                val completadosCount = rutina.ejercicios.count { completedExercises.contains(it.id) }
                val isFullyCompleted = totalEjercicios > 0 && completadosCount == totalEjercicios

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            expandedRoutineId = if (isExpanded) null else rutina.id
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF0F3F1),
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(TecsupGreenLight),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    imageVector = if (isFullyCompleted) Icons.Default.CheckCircle else Icons.Default.FitnessCenter,
                                    contentDescription = "Rutina",
                                    tint = TecsupGreenPrimary,
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = rutina.nombre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${rutina.nivel} • $completadosCount/$totalEjercicios ejercicios",
                                    color = if (isFullyCompleted) TecsupGreenPrimary else Color.Gray,
                                    fontSize = 13.sp,
                                    fontWeight = if (isFullyCompleted) FontWeight.Bold else FontWeight.Normal,
                                )
                            }

                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = "Expandir rutina",
                                tint = Color.Gray,
                            )
                        }

                        if (totalEjercicios > 0) {
                            Spacer(modifier = Modifier.height(12.dp))
                            LinearProgressIndicator(
                                progress = { completadosCount.toFloat() / totalEjercicios },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = TecsupGreenPrimary,
                                trackColor = Color(0xFFE0E0E0),
                            )
                        }

                        AnimatedVisibility(
                            visible = isExpanded,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut(),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                            ) {
                                HorizontalDivider(color = Color(0xFFE0E0E0))
                                Spacer(modifier = Modifier.height(8.dp))

                                rutina.ejercicios.forEach { ejercicio ->
                                    val isChecked = completedExercises.contains(ejercicio.id)

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                completedExercises = if (isChecked) {
                                                    completedExercises - ejercicio.id
                                                } else {
                                                    completedExercises + ejercicio.id
                                                }
                                            }
                                            .padding(vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Checkbox(
                                            checked = isChecked,
                                            onCheckedChange = { checked ->
                                                completedExercises = if (checked == true) {
                                                    completedExercises + ejercicio.id
                                                } else {
                                                    completedExercises - ejercicio.id
                                                }
                                            },
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = TecsupGreenPrimary,
                                            ),
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = ejercicio.nombre,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium,
                                                textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
                                                color = if (isChecked) Color.Gray else Color.Unspecified,
                                            )
                                            Text(
                                                text = ejercicio.repeticiones,
                                                fontSize = 12.sp,
                                                color = Color.Gray,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}