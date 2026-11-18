package com.tecsup.mibodega.ui.cliente

object Rutas {
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val INICIO = "inicio"
    const val DETALLE = "detalle/{productoId}"
    const val CARRITO = "carrito"
    const val DATOS_ENTREGA = "datos_entrega"
    const val CONFIRMACION = "confirmacion"
    const val PERFIL = "perfil"
    const val MIS_PEDIDOS = "mis_pedidos"
    const val FAVORITOS = "favoritos"

    fun detalle(productoId: Int): String = "detalle/$productoId"
}
