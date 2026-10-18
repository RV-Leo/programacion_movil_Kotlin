package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.model.EstadoPedido
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class PedidosAdminViewModel(private val repo: CafeRepository) : ViewModel() {
    val estadoSeleccionado = MutableStateFlow(EstadoPedido.RECIBIDO)

    val pedidos: StateFlow<List<PedidoConDetalle>> = estadoSeleccionado
        .flatMapLatest { estado -> repo.observarPedidosPorEstado(estado) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun seleccionarEstado(estado: EstadoPedido) {
        estadoSeleccionado.value = estado
    }

    fun avanzar(pedido: PedidoConDetalle) {
        val siguiente = EstadoPedido.desde(pedido.pedido.estado).siguiente() ?: return
        viewModelScope.launch {
            repo.cambiarEstado(pedido.pedido.id, siguiente)
        }
    }

    fun cancelar(pedido: PedidoConDetalle) {
        val estadoActual = EstadoPedido.desde(pedido.pedido.estado)
        if (estadoActual == EstadoPedido.ENTREGADO || estadoActual == EstadoPedido.CANCELADO) return
        viewModelScope.launch {
            repo.cambiarEstado(pedido.pedido.id, EstadoPedido.CANCELADO)
        }
    }
}


