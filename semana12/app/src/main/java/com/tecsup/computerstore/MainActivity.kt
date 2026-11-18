package com.tecsup.computerstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.tecsup.computerstore.ui.navigation.ComputerStoreApp
import com.tecsup.computerstore.ui.theme.ComputerStoreTheme
import androidx.core.content.edit

class MainActivity : ComponentActivity() {
    private val appContainer by lazy { DefaultAppContainer(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val preferences = getSharedPreferences("computer_store_preferences", MODE_PRIVATE)
        setContent {
            var darkMode by remember { mutableStateOf(preferences.getBoolean("dark_mode", false)) }
            ComputerStoreTheme(darkTheme = darkMode, dynamicColor = false) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    ComputerStoreApp(
                        appContainer = appContainer,
                        darkMode = darkMode,
                        onDarkModeChange = { enabled ->
                            darkMode = enabled
                            preferences.edit { putBoolean("dark_mode", enabled) }
                        }
                    )
                }
            }
        }
    }
}
