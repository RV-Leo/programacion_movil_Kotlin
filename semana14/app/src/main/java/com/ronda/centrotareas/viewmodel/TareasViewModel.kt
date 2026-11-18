package com.ronda.centrotareas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ronda.centrotareas.data.TareasRepository
import com.ronda.centrotareas.model.CargaUi
import com.ronda.centrotareas.model.PrimosUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

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
    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    private val _simularError = MutableStateFlow(false)
    val simularError = _simularError.asStateFlow()
    val carga = _carga.asStateFlow()

    fun tareaLarga() {
        if (_corriendo.value) return
        trabajo = viewModelScope.launch {
            _corriendo.value = true
            _progreso.value = 0f
            _mensaje.value = "Trabajando..."
            try {
                android.util.Log.d("HILO", "Antes: ${Thread.currentThread().name}")
                repo.progresoTarea().collect { _progreso.value = it }
                android.util.Log.d("HILO", "Después: ${Thread.currentThread().name}")
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

'    fun cancelarPrimos() { trabajoPrimos?.cancel() }

    fun cambiarSimularError(valor: Boolean) { _simularError.value = valor }
    fun cargarUnaPorUna() = cargar(paralelo = false)
    fun cargarALaVez() = cargar(paralelo = true)

    private fun cargar(paralelo: Boolean) {
        if (_carga.value == CargaUi.Cargando) return
        val simular = _simularError.value
        viewModelScope.launch {
            _carga.value = CargaUi.Cargando
            val inicio = System.currentTimeMillis()
            try {
                val (perfil, pedidos) = withTimeout(2_000) {
                    if (paralelo) coroutineScope {
                        val perfilDeferred = async { repo.cargarPerfil() }
                        val pedidosDeferred = async { repo.cargarPedidos(simular) }
                        perfilDeferred.await() to pedidosDeferred.await()
                    } else {
                        val perfilCargado = repo.cargarPerfil()
                        val pedidosCargados = repo.cargarPedidos(simular)
                        perfilCargado to pedidosCargados
                    }
                }
                val modo = if (paralelo) "A la vez" else "Una por una"
                _carga.value = CargaUi.Exito(modo, perfil, pedidos.size, System.currentTimeMillis() - inicio)
            } catch (e: TimeoutCancellationException) {
                _carga.value = CargaUi.Error("Tardó demasiado")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _carga.value = CargaUi.Error("No se pudo cargar: ${e.message}")
            }
        }
    }
}


