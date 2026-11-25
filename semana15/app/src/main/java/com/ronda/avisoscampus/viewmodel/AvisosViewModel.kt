package com.ronda.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ronda.avisoscampus.data.AvisosRepository
import com.ronda.avisoscampus.data.AvisosRepositoryLocal
import com.ronda.avisoscampus.model.Aviso
import com.ronda.avisoscampus.model.OrigenAviso
import java.util.concurrent.atomic.AtomicInteger
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class EstadoPermiso(val etiqueta: String) {
    SIN_PEDIR("Sin pedir"),
    PERMITIDO("Permitido"),
    DENEGADO("Denegado")
}

class AvisosViewModel(
    private val repository: AvisosRepository = AvisosRepositoryLocal()
) : ViewModel() {
    private val _estadoPermiso = MutableStateFlow(EstadoPermiso.SIN_PEDIR)
    val estadoPermiso: StateFlow<EstadoPermiso> = _estadoPermiso.asStateFlow()
    private val _eventos = MutableSharedFlow<Aviso>()
    val eventos = _eventos.asSharedFlow()
    private val _escuchandoAvion = MutableStateFlow(false)
    val escuchandoAvion: StateFlow<Boolean> = _escuchandoAvion.asStateFlow()
    private var trabajo: Job? = null
    private val _faltan = MutableStateFlow<Int?>(null)
    val faltan: StateFlow<Int?> = _faltan.asStateFlow()

    fun permisoRespondido(concedido: Boolean) {
        _estadoPermiso.value = if (concedido) EstadoPermiso.PERMITIDO else EstadoPermiso.DENEGADO
    }

    fun avisarAhora() {
        val aviso = Aviso(
            id = siguienteId.incrementAndGet(),
            titulo = "Tarea lista",
            texto = "Tu entrega fue registrada",
            origen = OrigenAviso.BOTON
        )
        viewModelScope.launch {
            repository.registrar(aviso)
            _eventos.emit(aviso)
        }
    }

    fun recordarEn(segundos: Int = 10) {
        if (trabajo?.isActive == true || segundos <= 0) return

        trabajo = viewModelScope.launch {
            try {
                for (restantes in segundos downTo 1) {
                    _faltan.value = restantes
                    delay(1_000)
                }

                val aviso = Aviso(
                    id = siguienteId.incrementAndGet(),
                    titulo = "Recordatorio",
                    texto = "Revisa tu laboratorio pendiente",
                    origen = OrigenAviso.RECORDATORIO
                )
                repository.registrar(aviso)
                _eventos.emit(aviso)
            } finally {
                _faltan.value = null
            }
        }
    }

    fun cancelarRecordatorio() {
        trabajo?.cancel()
    }

    fun cambiarEscucha(valor: Boolean) {
        _escuchandoAvion.value = valor
    }

    fun alCambiarModoAvion(activo: Boolean) {
        if (!_escuchandoAvion.value) return

        val aviso = Aviso(
            id = siguienteId.incrementAndGet(),
            titulo = "Modo avión",
            texto = if (activo) "Activado" else "Desactivado",
            origen = OrigenAviso.MODO_AVION
        )
        viewModelScope.launch {
            repository.registrar(aviso)
            _eventos.emit(aviso)
        }
    }

    private companion object {
        val siguienteId = AtomicInteger(0)
    }
}
