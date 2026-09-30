package com.ronda.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.ronda.navlab.data.StudentRepository
import com.ronda.navlab.navigation.Screen
import com.ronda.navlab.ui.theme.NavLabTheme
import com.ronda.navlab.ui.theme.PurpleDark
import com.ronda.navlab.ui.theme.PurpleMedium
import com.ronda.navlab.ui.theme.PurplePrimary
import com.ronda.navlab.ui.theme.PurpleUltraLight
import com.ronda.navlab.ui.theme.ScreenBackgroundLight
import com.ronda.navlab.ui.theme.TextSecondaryGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController) {
    val students = StudentRepository.sampleStudents

    Scaffold(
        containerColor = ScreenBackgroundLight,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Directorio de Alumnos",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = PurpleDark,
                            fontSize = 20.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = PurpleDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ScreenBackgroundLight
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(students) { student ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Screen.Detail.createRoute(student.id))
                        },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Circle Avatar with initials
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(
                                    color = PurpleUltraLight,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = student.avatarInitials,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PurplePrimary,
                                    fontSize = 18.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Student Name and Career
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = student.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PurpleDark,
                                    fontSize = 16.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = student.career,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = TextSecondaryGray,
                                    fontSize = 13.sp
                                )
                            )
                        }

                        // Right Chevron indicator
                        Text(
                            text = "›",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Light,
                                color = PurpleMedium,
                                fontSize = 26.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    NavLabTheme {
        ListScreen(rememberNavController())
    }
}

