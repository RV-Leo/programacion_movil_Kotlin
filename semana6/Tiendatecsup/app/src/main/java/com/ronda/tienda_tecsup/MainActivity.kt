package com.ronda.tienda_tecsup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ronda.tienda_tecsup.ui.theme.TiendatecsupTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TiendatecsupTheme {
                AppNavegacion()
            }
        }
    }
}
