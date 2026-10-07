package com.tecsup.mibodega.ui.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.theme.RojoPrecio

/**
 * Input con label arriba (fuera del recuadro), como en los mockups
 * de Registro y Datos de entrega.
 *
 * @param teclado tipo de teclado, ej. KeyboardType.Phone para el teléfono
 * @param isError indica si el campo tiene un error (marca el borde y etiqueta en rojo)
 * @param errorMessage mensaje opcional de error debajo del campo
 */
@Composable
fun CampoTexto(
    etiqueta: String,
    valor: String,
    onValorCambia: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    teclado: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = if (isError) RojoPrecio else MaterialTheme.colorScheme.onBackground
        )
        OutlinedTextField(
            value = valor,
            onValueChange = onValorCambia,
            modifier = Modifier.fillMaxWidth(),
            placeholder = placeholder?.let { { Text(it) } },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = teclado),
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedBorderColor = if (isError) RojoPrecio else MaterialTheme.colorScheme.outline,
                focusedBorderColor = if (isError) RojoPrecio else MaterialTheme.colorScheme.primary,
                errorBorderColor = RojoPrecio
            )
        )
        if (isError && !errorMessage.isNullOrBlank()) {
            Spacer(Modifier.height(2.dp))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = RojoPrecio
            )
        }
    }
}
