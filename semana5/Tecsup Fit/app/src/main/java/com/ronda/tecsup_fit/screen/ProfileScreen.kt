package com.ronda.tecsup_fit.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ronda.tecsup_fit.ui.theme.TecsupGreenLight
import com.ronda.tecsup_fit.ui.theme.TecsupGreenPrimary

@Composable
fun ProfileScreen(
    @Suppress("UNUSED_PARAMETER") navController: NavController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Mi perfil",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start),
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Avatar Circle
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(TecsupGreenLight),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "LR",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TecsupGreenPrimary,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Leonardo Ronda",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Plan Premium",
            fontSize = 14.sp,
            color = Color.Gray,
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Stats Cards Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF0F3F1),
                ),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "14",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Clases",
                        fontSize = 13.sp,
                        color = Color.Gray,
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF0F3F1),
                ),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "3",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Rachas",
                        fontSize = 13.sp,
                        color = Color.Gray,
                    )
                }
            }
        }
    }
}
