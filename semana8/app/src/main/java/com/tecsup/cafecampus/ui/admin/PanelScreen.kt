package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tecsup.cafecampus.viewmodel.PanelAdminViewModel

@Composable
fun PanelScreen(vm: PanelAdminViewModel) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Panel de Administración")
    }
}
