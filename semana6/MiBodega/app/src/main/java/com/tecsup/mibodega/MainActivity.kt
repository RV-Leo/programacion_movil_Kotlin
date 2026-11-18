package com.tecsup.mibodega

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.tecsup.mibodega.ui.cliente.AppNavegacion
import com.tecsup.mibodega.ui.theme.BodegaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Aqui se gestiona el estado global del tema oscuro para toda la app
            var modoOscuro by remember { mutableStateOf(false) }

            BodegaTheme(darkTheme = modoOscuro) {
                AppNavegacion(
                    modoOscuro = modoOscuro,
                    onModoOscuroChanged = { modoOscuro = it }
                )
            }
        }
    }
}
