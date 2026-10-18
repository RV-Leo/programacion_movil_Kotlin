package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.tecsup.cafecampus.viewmodel.MenuViewModel

@Composable
fun MenuScreen(vm: MenuViewModel, onProducto: (Int) -> Unit) {
    val productos by vm.productos.collectAsState()
    TECSUP · Programación en Móviles · Laboratorio MVVM + Room (CafeCampus) · Pág. 7
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(productos) { p -> ProductoCard(p) { onProducto(p.id) } }
    }
}