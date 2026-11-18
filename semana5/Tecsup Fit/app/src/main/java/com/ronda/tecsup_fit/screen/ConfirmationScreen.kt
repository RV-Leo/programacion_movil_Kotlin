package com.ronda.tecsup_fit.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import com.ronda.tecsup_fit.data.ReservationRepository
import com.ronda.tecsup_fit.navigation.Screen
import com.ronda.tecsup_fit.ui.theme.TecsupGreenLight
import com.ronda.tecsup_fit.ui.theme.TecsupGreenPrimary

@Composable
fun ConfirmationScreen(
    navController: NavController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Circle with Checkmark
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(TecsupGreenLight),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Éxito",
                tint = TecsupGreenPrimary,
                modifier = Modifier.size(40.dp),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "¡Cupo reservado!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = ReservationRepository.lastReservedName,
            fontSize = 15.sp,
            color = Color.Gray,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = ReservationRepository.lastReservedDetails,
            fontSize = 14.sp,
            color = Color.Gray,
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                navController.navigate(Screen.Reservations.route)
            },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE8ECE9),
                contentColor = Color.Black,
            ),
            modifier = Modifier.height(44.dp),
        ) {
            Text(
                text = "Ver mis reservas",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}
