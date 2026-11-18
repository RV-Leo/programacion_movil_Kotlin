package com.ronda.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TareasViewModel : ViewModel() {
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje = _mensaje.asStateFlow()
    private val _progreso = MutableStateFlow(0f)
    val progreso = _progreso.asStateFlow()

    fun tareaLarga() {
        viewModelScope.launch {
            _progreso.value = 0f
            _mensaje.value = "Trabajando..."
            Log.d("HILO", "Antes: ${Thread.currentThread().name}")
            for (i in 1..100) {
                withContext(Dispatchers.Default) {
                    if (i == 1) Log.d("HILO", "Dentro: ${Thread.currentThread().name}")
                    delay(40)
                }
                _progreso.value = i / 100f
            }
            Log.d("HILO", "Después: ${Thread.currentThread().name}")
            _mensaje.value = "Terminó"
        }
    }
}
