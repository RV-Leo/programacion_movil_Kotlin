package com.tecsup.cafecampus.data.repository

import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.local.entity.Usuario
import kotlinx.coroutines.flow.Flow

interface CafeRepository {
    // Usuarios
    suspend fun login(correo: String, clave: String): Usuario?
    // Productos (ejemplo resuelto)
    fun observarProductos(): Flow<List<Producto>>
    suspend fun guardarProducto(p: Producto)
    suspend fun eliminarProducto(p: Producto)
    suspend fun cambiarDisponibilidad(id: Int, disponible: Boolean)
    // Pedidos
    suspend fun crearPedido(clienteId: Int, items: List<ItemCarrito>,
                            horaRecojo: String, pago: String, nota: String): Long
    fun observarPedidosCliente(clienteId: Int): Flow<List<PedidoConDetalle>>
    fun observarPedidosPorEstado(estado: EstadoPedido): Flow<List<PedidoConDetalle>>
    fun observarPedido(id: Int): Flow<PedidoConDetalle?>
    suspend fun cambiarEstado(pedidoId: Int, estado: EstadoPedido)
    // Panel
    fun observarVentasHoy(inicioDia: Long): Flow<Double>
    fu