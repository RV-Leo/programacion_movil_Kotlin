package com.ronda.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ronda.navlab.navigation.Screen
import com.ronda.navlab.ui.theme.AppBackgroundGradient
import com.ronda.navlab.ui.theme.LogoutRed
import com.ronda.navlab.ui.theme.NavLabTheme
import com.ronda.navlab.ui.theme.PurpleDark
import com.ronda.navlab.ui.theme.PurplePrimary
import com.ronda.navlab.ui.theme.PurpleUltraLight
import com.ronda.navlab.ui.theme.TextSecondaryGray

@Composable
fun HomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundGradient)
            .statusBarsPadding()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Text Header
            Text(
                text = "Bienvenido,\nLeonardo Ronda",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 30.sp,
                    lineHeight = 36.sp
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "¿Qué deseas gestionar hoy?",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFFE2D8F8),
                    fontSize = 15.sp
                )
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Card 1: Directorio de Alumnos
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(Screen.List.route) },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(
                                color = PurpleUltraLight,
                                shape = RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBox,
                            contentDescription = "Directorio",
                            tint = PurplePrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Directorio de Alumnos",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PurpleDark,
                                fontSize = 16.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Ver y gestionar estudiantes",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextSecondaryGray,
                                fontSize = 13.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card 2: Mi Perfil Académico
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(Screen.Profile.route) },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(
                                color = PurpleUltraLight,
                                shape = RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Mi Perfil",
                            tint = PurplePrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Mi Perfil Académico",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PurpleDark,
                                fontSize = 16.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Datos personales y progreso",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextSecondaryGray,
                                fontSize = 13.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Logout Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Cerrar sesión",
                    tint = LogoutRed,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cerrar Sesión Segura",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = LogoutRed,
                        fontSize = 15.sp
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    NavLabTheme {
        HomeScreen(rememberNavController())
    }
}

