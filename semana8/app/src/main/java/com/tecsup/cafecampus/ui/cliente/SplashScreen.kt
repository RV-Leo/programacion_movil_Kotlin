package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.viewmodel.SesionViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    vm: SesionViewModel,
    onNavigate: (String) -> Unit
) {
    val perfilActivo by vm.perfilActivo.collectAsState()

    LaunchedEffect(perfilActivo) {
        delay(800)
        when {
            perfilActivo.equals(Rol.CLIENTE.name, ignoreCase = true) -> onNavigate("menu")
            perfilActivo.equals(Rol.ADMIN.name, ignoreCase = true) -> onNavigate("panel")
            else -> onNavigate("login")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
