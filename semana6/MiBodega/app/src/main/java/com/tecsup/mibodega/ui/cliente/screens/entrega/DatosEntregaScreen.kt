package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.CampoTexto
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.VerdeBodega

@Composable
fun DatosEntregaScreen(
    totalPedido: Double,
    onVolver: () -> Unit,
    onConfirmar: (String, String, String, String, Double) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var referencia by remember { mutableStateOf("") }
    var esDelivery by remember { mutableStateOf(true) }

    var intentarEnviar by remember { mutableStateOf(false) }

    val nombreError = intentarEnviar && nombre.isBlank()
    val telefonoError = intentarEnviar && telefono.isBlank()
    val direccionError = intentarEnviar && direccion.isBlank()

    val costoEnvio = if (esDelivery) 4.00 else 0.00
    val totalFinal = totalPedido + costoEnvio

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver al carrito")
            }
            Text("Datos de entrega", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(Modifier.height(8.dp))
        Text(
            text = "Completa la información y elige el método de entrega.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(16.dp))

        // Opciones de entrega con RadioButton
        Text(
            text = "Método de entrega",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(4.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { esDelivery = true }
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = esDelivery,
                onClick = { esDelivery = true }
            )
            Spacer(Modifier.width(8.dp))
            Column {
                Text(text = "Delivery a domicilio", fontWeight = FontWeight.SemiBold)
                Text(text = "Costo: S/ 4.00", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { esDelivery = false }
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = !esDelivery,
                onClick = { esDelivery = false }
            )
            Spacer(Modifier.width(8.dp))
            Column {
                Text(text = "Recojo en tienda", fontWeight = FontWeight.SemiBold)
                Text(text = "Costo: Gratis (S/ 0.00)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Nombre de quien recibe",
            valor = nombre,
            onValorCambia = { nombre = it },
            placeholder = "Nombre completo",
            isError = nombreError,
            errorMessage = if (nombreError) "Campo obligatorio" else null
        )
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Teléfono",
            valor = telefono,
            onValorCambia = { telefono = it },
            placeholder = "987 654 321",
            teclado = KeyboardType.Phone,
            isError = telefonoError,
            errorMessage = if (telefonoError) "Campo obligatorio" else null
        )
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Dirección",
            valor = direccion,
            onValorCambia = { direccion = it },
            placeholder = "Av. Los Olivos 123",
            isError = direccionError,
            errorMessage = if (direccionError) "Campo obligatorio" else null
        )
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Referencia (opcional)",
            valor = referencia,
            onValorCambia = { referencia = it },
            placeholder = "Frente al parque"
        )
        Spacer(Modifier.height(20.dp))

        // Resumen de costos
        Text(
            text = "Subproductos: S/ %.2f".format(totalPedido),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Entrega (${if (esDelivery) "Delivery" else "Recojo"}): S/ %.2f".format(costoEnvio),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Total a pagar: S/ %.2f".format(totalFinal),
            style = MaterialTheme.typography.titleMedium,
            color = VerdeBodega,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(20.dp))

        BotonPrimario(
            texto = "Confirmar pedido",
            onClick = {
                intentarEnviar = true
                if (nombre.isNotBlank() && telefono.isNotBlank() && direccion.isNotBlank()) {
                    onConfirmar(nombre.trim(), telefono.trim(), direccion.trim(), referencia.trim(), totalFinal)
                }
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DatosEntregaPreview() {
    BodegaTheme {
        DatosEntregaScreen(totalPedido = 25.50, onVolver = {}, onConfirmar = { _, _, _, _, _ -> })
    }
}
