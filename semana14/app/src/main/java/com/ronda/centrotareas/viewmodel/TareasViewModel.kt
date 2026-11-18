package com.ronda.centrotareas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ronda.centrotareas.data.TareasRepository
import com.ronda.centrotareas.model.CargaUi
import com.ronda.centrotareas.model.PrimosUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje = _mensaje.asStateFlow()
    private val _progreso = MutableStateFlow(0f)
    val progreso = _progreso.asStateFlow()
    private val _corriendo = MutableStateFlow(false)
    val corriendo = _corriendo.asStateFlow()
    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos = _primos.asStateFlow()
    private var trabajo: Job? = null
    private var trabajoPrimos: Job? = null

    fun tareaLarga() {
        if (_corriendo.value) return
        trabajo = viewModelScope.launch {
            _corriendo.value = true
            _progreso.value = 0f
            _mensaje.value = "Trabajando..."
            try {
                for (i in 1..100) {
                    withContext(Dispatchers.Default) { delay(40) }
                    _progreso.value = i / 100f
                }
                _mensaje.value = "Terminó"
            } catch (e: CancellationException) {
                throw e
            } finally {
                _corriendo.value = false
            }
        }
    }

    fun cancelar() {
        if (_corriendo.value) {
            trabajo?.cancel()
            _mensaje.value = "Cancelado"
        }
    }

    fun contar(hasta: Int) {
        if (_primos.value == PrimosUi.Calculando) return
        trabajoPrimos = viewModelScope.launch {
            _primos.value = PrimosUi.Calculando
            val inicio = System.currentTimeMillis()
            try {
                val cantidad = repo.contarPrimos(hasta)
                _primos.value = PrimosUi.Listo(cantidad, System.currentTimeMillis() - inicio)
            } catch (e: CancellationException) {
                _primos.value = PrimosUi.Cancelado
                throw e
            }
        }
    }

    fun cancelarPrimos() { trabajoPrimos?.cancel() }
}
