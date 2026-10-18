package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.model.ItemCarrito
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PedidosClienteViewModel(private val repo: CafeRepository) : ViewModel() {
    private val clienteId = MutableStateFlow(0)
    private val _misPedidos = MutableStateFlow<List<PedidoConDetalle>>(emptyList())
    val misPedidos: StateFlow<List<PedidoConDetalle>> = _misPedidos.asStateFlow()

    private val _confirmando = MutableStateFlow(false)
    val confirmando: StateFlow<Boolean> = _confirmando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var pedidosJob: Job? = null

    fun establecerCliente(id: Int) {
        if (id <= 0 || clienteId.value == id) return
        clienteId.value = id
        pedidosJob?.cancel()
        pedidosJob = viewModelScope.launch {
            repo.observarPedidosCliente(id).collect { _misPedidos.value = it }
        }
    }

    fun pedido(id: Int): StateFlow<PedidoConDetalle?> = repo.observarPedido(id)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun confirmar(
        items: List<ItemCarrito>,
        horaRecojo: String,
        pago: String,
        nota: String,
        onConfirmado: (Long) -> Unit
    ) {
        if (_confirmando.value) return
        if (clienteId.value <= 0) {
            _error.value = "No se encontró la sesión del estudiante. Vuelve a iniciar sesión."
            return
        }
        if (items.isEmpty()) {
            _error.value = "Agrega al menos un producto al pedido."
            return
        }

        viewModelScope.launch {
            _confirmando.value = true
            _error.value = null
            try {
                val pedidoId = repo.crearPedido(
                    clienteId = clienteId.value,
                    items = items,
                    horaRecojo = horaRecojo,
                    pago = pago,
                    nota = nota.trim()
                )
                onConfirmado(pedidoId)
            } catch (e: Exception) {
                _error.value = e.message ?: "No se pudo confirmar el pedido. Intenta de nuevo."
            } finally {
                _confirmando.value = false
            }
        }
    }
}
