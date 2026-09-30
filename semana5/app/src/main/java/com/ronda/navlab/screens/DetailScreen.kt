package com.ronda.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ronda.navlab.data.StudentRepository
import com.ronda.navlab.ui.theme.BorderLight
import com.ronda.navlab.ui.theme.DetailBannerGradient
import com.ronda.navlab.ui.theme.NavLabTheme
import com.ronda.navlab.ui.theme.PurpleDark
import com.ronda.navlab.ui.theme.PurplePrimary
import com.ronda.navlab.ui.theme.PurpleUltraLight
import com.ronda.navlab.ui.theme.ScreenBackgroundLight
import com.ronda.navlab.ui.theme.TextSecondaryGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, itemId: Int) {
    val student = StudentRepository.getStudentById(itemId)

    Scaffold(
        containerColor = ScreenBackgroundLight,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Expediente Académico",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Banner & Overlapping Avatar Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                // Top Gradient Curved Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                        .background(DetailBannerGradient)
                )

                // Overlapping Avatar Circle
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .size(100.dp)
                        .background(color = PurpleUltraLight, shape = CircleShape)
                        .border(width = 4.dp, color = Color.White, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = student.avatarInitials,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PurplePrimary,
                            fontSize = 32.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Student Name and Career Header
            Text(
                text = student.name,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = PurpleDark,
                    fontSize = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = student.career,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = PurplePrimary,
                    fontSize = 15.sp
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Information Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                ) {
                    // Item 1: ID Estudiante
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(PurpleUltraLight, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountBox,
                                contentDescription = null,
                                tint = PurplePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "ID Estudiante",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextSecondaryGray,
                                    fontSize = 11.sp
                                )
                            )
                            Text(
                                text = student.studentCode,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PurpleDark,
                                    fontSize = 14.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Item 2: Correo Electrónico
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(PurpleUltraLight, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = PurplePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Correo Electrónico",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextSecondaryGray,
                                    fontSize = 11.sp
                                )
                            )
                            Text(
                                text = student.email,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PurpleDark,
                                    fontSize = 14.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Item 3: Facultad
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(PurpleUltraLight, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = PurplePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Facultad",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextSecondaryGray,
                                    fontSize = 11.sp
                                )
                            )
                            Text(
                                text = student.faculty,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PurpleDark,
                                    fontSize = 14.sp
                                )
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 18.dp),
                        color = BorderLight
                    )

                    // Biografía
                    Text(
                        text = "Biografía",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PurpleDark,
                            fontSize = 15.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = student.bio,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondaryGray,
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    NavLabTheme {
        DetailScreen(rememberNavController(), itemId = 1)
    }
}

