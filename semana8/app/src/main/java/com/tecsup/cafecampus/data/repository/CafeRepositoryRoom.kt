package com.tecsup.cafecampus.data.repository

import com.tecsup.cafecampus.data.local.dao.PedidoDao
import com.tecsup.cafecampus.data.local.dao.ProductoDao
import com.tecsup.cafecampus.data.local.dao.UsuarioDao
import com.tecsup.cafecampus.data.local.entity.DetallePedido
import com.tecsup.cafecampus.data.local.entity.Pedido
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.local.entity.Usuario
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.Flow

class CafeRepositoryRoom(
    private val productoDao: ProductoDao,
    private val usuarioDao: UsuarioDao,
    private val pedidoDao: PedidoDao
) : CafeRepository {

    // Usuarios
    override suspend fun login(correo: String, clave: String): Usuario? {
        return usuarioDao.login(correo, clave)
    }

    // Productos
    override fun observarProductos(): Flow<List<Producto>> {
        return productoDao.observarTodos()
    }

    override suspend fun guardarProducto(p: Producto) {
        if (p.id == 0) {
            productoDao.insertar(p)
        } else {
            productoDao.actualizar(p)
        }
    }

    override suspend fun eliminarProducto(p: Producto) {
        productoDao.eliminar(p)
    }

    override suspend fun cambiarDisponibilidad(id: Int, disponible: Boolean) {
        productoDao.cambiarDisponibilidad(id, disponible)
    }

    // Pedidos
    override suspend fun crearPedido(
        clienteId: Int,
        items: List<ItemCarrito>,
        horaRecojo: String,
        pago: String,
        nota: String
    ): Long {
        val totalCalculado = items.sumOf { it.producto.precio * it.cantidad }

        val pedido = Pedido(
            clienteId = clienteId,
            total = totalCalculado,
            horaRecojo = horaRecojo,
            pago = pago,
            nota = nota,
            estado = EstadoPedido.PENDIENTE.name
        )

        val detalles = items.map { item ->
            DetallePedido(
                pedidoId = 0,
                productoId = item.producto.id,
                nombreProducto = item.producto.nombre,
                cantidad = item.cantidad,
                precioUnitario = item.producto.precio
            )
        }

        return pedidoDao.crearPedido(pedido, detalles)
    }

    override fun observarPedidosCliente(clienteId: Int): Flow<List<PedidoConDetalle>> {
        return pedidoDao.observarPorCliente(clienteId)
    }

    override fun observarPedidosPorEstado(estado: EstadoPedido): Flow<List<PedidoConDetalle>> {
        return pedidoDao.observarPorEstado(estado.name)
    }

    override fun observarPedido(id: Int): Flow<PedidoConDetalle?> {
        return pedidoDao.observarUno(id)
    }

    override suspend fun cambiarEstado(pedidoId: Int, estado: EstadoPedido) {
        pedidoDao.cambiarEstado(pedidoId, estado.name)
    }

    // Panel
    override fun observarVentasHoy(inicioDia: Long): Flow<Double> {
        return pedidoDao.ventasDesde(inicioDia)
    }

    override fun observarPedidosHoy(inicioDia: Long): Flow<Int> {
        return pedidoDao.pedidosDesde(inicioDia)
    }
}
