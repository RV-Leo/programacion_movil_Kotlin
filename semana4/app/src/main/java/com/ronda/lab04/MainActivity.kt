package com.ronda.lab04


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ronda.lab04.ui.theme.Lab04Theme
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab04Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        TemperatureDisplay()
                    }
                }
            }
        }
    }
}

@Composable
fun ContadorRoto() {
    var contador = 0
    Column {
        Text("Contador: $contador")
        Button(onClick = { contador++ }) {
            Text("Incrementar")
        }
    }
}

@Composable
fun ContadorConRemember(){
    var contador by remember {mutableStateOf(0)}
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text="Contador: $contador",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier=Modifier.height(8.dp))
        Button(onClick = {contador++}) {
            Text("Incrementar")
        }
    }
}

@Composable
fun TemperatureDisplay(){
    var temperatura by remember {mutableStateOf(20)}
    val numberColor = when{
        temperatura > 30 -> Color.Red
        temperatura < 10 -> Color.Blue
        else -> Color.Unspecified
    }
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = buildAnnotatedString {
                append("Temperatura: ")

                // Cambia el color solo al numero
                withStyle(style = SpanStyle(color = numberColor)) {
                    append("$temperatura °C")
                }
            },
            fontWeight = FontWeight.Bold,
            style= MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){

            Button(onClick = {temperatura++},
                modifier = Modifier.weight(1f)) {
                Text("Subir")
            }

            Button(onClick = {temperatura--},
                modifier = Modifier.weight(1f)) {
                Text("Bajar")
            }

            Button(onClick = {temperatura = 20},
                modifier = Modifier.weight(1f)) {
                Text("Resetear")
            }
        }

    }
}
