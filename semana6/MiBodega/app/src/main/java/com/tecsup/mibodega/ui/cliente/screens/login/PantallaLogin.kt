package com.tecsup.mibodega.ui.cliente.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.R
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.CampoTexto
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.RojoPrecio
import com.tecsup.mibodega.ui.theme.VerdeBodega

@Composable
fun PantallaLogin(
    onIngresar: () -> Unit,
    onCrearCuenta: () -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var errorMensaje by remember { mutableStateOf<String?>(null) }

    // Credenciales fijas para el inicio de sesión
    val correoPrueba= "leo@bodega.com"
    val contrasenaPrueba = "1234"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ilustracion_bodega),
            contentDescription = "Ilustración Bodega",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(5.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Mi Bodega",
            style = MaterialTheme.typography.displaySmall,
            color = VerdeBodega
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Inicia sesión para continuar",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(6.dp))
        //Texto por si me olvido las credenciales
        Text(
            text = "Usuario y contraseña: $correoPrueba / $contrasenaPrueba",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(20.dp))
        //Aqui se ingresa el correo
        CampoTexto(
            etiqueta = "Correo electrónico",
            valor = correo,
            onValorCambia = {
                correo = it
                errorMensaje = null
            },
            placeholder = "nombre@correo.com",
            teclado = KeyboardType.Email
        )
        Spacer(Modifier.height(16.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Contraseña",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            //Aqui se ingresa la contraseña
            OutlinedTextField(
                value = contrasena,
                onValueChange = {
                    contrasena = it
                    errorMensaje = null
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Ingresa tu contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        }

        if (errorMensaje != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = errorMensaje!!,
                style = MaterialTheme.typography.bodySmall,
                color = RojoPrecio
            )
        }

        Spacer(Modifier.height(24.dp))
        //Aqui se valida las credenciales
        BotonPrimario(
            texto = "Iniciar sesión",
            onClick = {
                if (correo.trim().lowercase() == correoPrueba && contrasena == contrasenaPrueba) {
                    errorMensaje = null
                    onIngresar()
                } else {
                    errorMensaje = "Correo o contraseña incorrectos"
                }
            },
            habilitado = correo.isNotBlank() && contrasena.isNotBlank()
        )
        TextButton(onClick = onCrearCuenta) {
            Text("¿No tienes cuenta? Crear cuenta")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PantallaLoginPreview() {
    BodegaTheme {
        PantallaLogin(onIngresar = {}, onCrearCuenta = {})
    }
}
