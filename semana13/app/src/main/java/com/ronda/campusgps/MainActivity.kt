package com.ronda.campusgps
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ronda.campusgps.ui.PantallaMapa
import com.ronda.campusgps.ui.theme.CampusGPSTheme
class MainActivity : ComponentActivity() {
 override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); enableEdgeToEdge(); setContent { CampusGPSTheme { PantallaMapa() } } }
}
