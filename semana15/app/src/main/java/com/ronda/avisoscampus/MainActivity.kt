package com.ronda.avisoscampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ronda.avisoscampus.ui.PantallaAvisos
import com.ronda.avisoscampus.ui.theme.AvisosCampusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AvisosCampusTheme {
                PantallaAvisos()
            }
        }
    }
}
