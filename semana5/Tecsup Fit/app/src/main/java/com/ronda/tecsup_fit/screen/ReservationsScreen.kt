package com.ronda.tecsup_fit.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventBusy
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
import com.ronda.tecsup_fit.data.ReservaItem
import com.ronda.tecsup_fit.data.ReservationRepository
import com.ronda.tecsup_fit.ui.theme.*

@Composable
fun ReservationsScreen(
    @Suppress("UNUSED_PARAMETER") navController: NavController,
) {
    var itemToCancel by remember { mutableStateOf<ReservaItem?>(null) }
    val list = ReservationRepository.reservations

    if (itemToCancel != null) {
        AlertDialog(
            onDismissRequest = { itemToCancel = null },
            title = { Text("Cancelar reserva") },
            text = { Text("¿Estás seguro de que deseas cancelar tu reserva para ${itemToCancel?.nombre}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        itemToCancel?.let { ReservationRepository.cancelReservation(it.id) }
                        itemToCancel = null
                    },
                ) {
                    Text("Sí, cancelar", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToCancel = null }) {
                    Text("No, mantener")
                }
            },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp),
    ) {
        Text(
            text = "Mis reservas",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (list.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.EventBusy,
                        contentDescription = "Sin reservas",
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp),
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No tienes reservas registradas",
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(list, key = { it.id }) { reserva ->
                    val isConfirmada = reserva.estado == "Confirmada"

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF0F3F1),
                        ),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                        ) {
                            if (isConfirmada) {
                                Box(
                                    modifier = Modifier
                                        .width(5.dp)
                                        .fillMaxHeight()
                                        .background(TecsupGreenPrimary),
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = reserva.nombre,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                    )

                                    if (isConfirmada) {
                                        TextButton(
                                            onClick = { itemToCancel = reserva },
                                            contentPadding = PaddingValues(0.dp),
                                        ) {
                                            Text(
                                                text = "Cancelar",
                                                color = Color.Red,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = reserva.fechaHora,
                                    color = Color.Gray,
                                    fontSize = 13.sp,
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                val badgeBg = when (reserva.estado) {
                                    "Confirmada" -> ConfirmadaGreenBg
                                    "Cancelada" -> CanceladaRedBg
                                    else -> CompletadaGrayBg
                                }

                                val badgeText = when (reserva.estado) {
                                    "Confirmada" -> ConfirmadaGreenText
                                    "Cancelada" -> CanceladaRedText
                                    else -> CompletadaGrayText
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(badgeBg)
                                        .padding(horizontal = 12.dp, vertical = 4.dp),
                                ) {
                                    Text(
                                        text = reserva.estado,
                                        color = badgeText,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
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