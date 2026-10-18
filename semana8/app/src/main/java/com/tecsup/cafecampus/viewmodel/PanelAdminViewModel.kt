package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.model.EstadoPedido
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

class PanelAdminViewModel(private val repo: CafeRepository) : ViewModel() {
    private val inicioDia: Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    val nuevos: StateFlow<Int> = repo.observarPedidosPorEstado(EstadoPedido.RECIBIDO)
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val preparando: StateFlow<Int> = repo.observarPedidosPorEstado(EstadoPedido.PREPARANDO)
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val listos: StateFlow<Int> = repo.observarPedidosPorEstado(EstadoPedido.LISTO)
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val ventasHoy: StateFlow<Double> = repo.observarVentasHoy(inicioDia)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    val pedidosHoy: StateFlow<Int> = repo.observarPedidosHoy(inicioDia)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)
}
