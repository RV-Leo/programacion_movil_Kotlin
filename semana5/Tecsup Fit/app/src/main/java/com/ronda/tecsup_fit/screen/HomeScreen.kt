package com.ronda.tecsup_fit.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ronda.tecsup_fit.navigation.Screen
import com.ronda.tecsup_fit.ui.theme.TecsupGreenLight
import com.ronda.tecsup_fit.ui.theme.TecsupGreenPrimary

data class Clase(
    val id: Int,
    val nombre: String,
    val hora: String,
    val sala: String,
    val duracion: String = "45 min",
    val descripcion: String = "Entrenamiento funcional de alta intensidad. Cupos limitados.",
    val cuposDisponibles: String = "8 de 12 cupos disponibles",
    val filtro: String = "Hoy",
)

val clases: List<Clase> = listOf(
    Clase(
        id = 1,
        nombre = "Yoga funcional",
        hora = "7:00 am",
        sala = "Sala 2",
        descripcion = "Sesión de flexibilidad y fuerza postural.",
        cuposDisponibles = "5 de 15 cupos disponibles",
        filtro = "Hoy",
    ),
    Clase(
        id = 2,
        nombre = "Cross Training",
        hora = "6:00 pm",
        sala = "Sala 1",
        descripcion = "Entrenamiento funcional de alta intensidad. Cupos limitados.",
        cuposDisponibles = "8 de 12 cupos disponibles",
        filtro = "Hoy",
    ),
    Clase(
        id = 3,
        nombre = "Spinning",
        hora = "7:30 pm",
        sala = "Sala 3",
        descripcion = "Clase de ciclismo de alta resistencia e intensidad.",
        cuposDisponibles = "3 de 10 cupos disponibles",
        filtro = "Esta semana",
    ),
)

@Composable
fun HomeScreen(navController: NavController) {
    var selectedFilter by remember { mutableStateOf("Hoy") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredClases = clases.filter { clase ->
        val matchesFilter = if (selectedFilter == "Hoy") clase.filtro == "Hoy" else true
        val matchesSearch = clase.nombre.contains(searchQuery, ignoreCase = true) ||
                clase.sala.contains(searchQuery, ignoreCase = true)
        matchesFilter && matchesSearch
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(TecsupGreenPrimary)
                .padding(horizontal = 20.dp, vertical = 24.dp),
        ) {
            Column {
                Text(
                    text = "TECSUP Fit",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Hola, Diego",
                    color = TecsupGreenLight,
                    fontSize = 14.sp,
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = { Text("Buscar clase o sala...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Color.Gray,
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpiar búsqueda",
                                tint = Color.Gray,
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TecsupGreenPrimary,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color(0xFFF0F3F1),
                    unfocusedContainerColor = Color(0xFFF0F3F1),
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            val filters = listOf("Hoy", "Esta semana")
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(filters) { filtro ->
                    val isSelected = selectedFilter == filtro
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = filtro },
                        label = {
                            Text(
                                text = filtro,
                                color = if (isSelected) Color.White else Color.Black,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TecsupGreenPrimary,
                            containerColor = Color(0xFFE8ECE9),
                        ),
                        border = null,
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Clases disponibles",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredClases.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = "No hay resultados",
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp),
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No se encontraron clases",
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Intenta buscar con otro término o cambia de filtro",
                            fontSize = 12.sp,
                            color = Color.Gray,
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(filteredClases) { clase ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(
                                        Screen.Detail.createRoute(clase.id),
                                    )
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFF0F3F1),
                            ),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
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
                                        imageVector = Icons.Default.FitnessCenter,
                                        contentDescription = "Clase",
                                        tint = TecsupGreenPrimary,
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    Text(
                                        text = clase.nombre,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${clase.hora} • ${clase.sala}",
                                        color = Color.Gray,
                                        fontSize = 13.sp,
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
