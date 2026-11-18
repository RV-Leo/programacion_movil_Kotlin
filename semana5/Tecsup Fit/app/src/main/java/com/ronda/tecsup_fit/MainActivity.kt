package com.ronda.tecsup_fit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ronda.tecsup_fit.navigation.AppNavigation
import com.ronda.tecsup_fit.ui.theme.TecsupFitTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TecsupFitTheme {
                AppNavigation()
            }
        }
    }
}
