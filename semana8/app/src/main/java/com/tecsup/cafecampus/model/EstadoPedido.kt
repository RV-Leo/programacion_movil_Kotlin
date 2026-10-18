package com.tecsup.cafecampus.model

enum class EstadoPedido {
    RECIBIDO,
    PREPARANDO,
    LISTO,
    ENTREGADO,
    CANCELADO;

    fun siguiente(): EstadoPedido? = when (this) {
        RECIBIDO -> PREPARANDO
        PREPARANDO -> LISTO
        LISTO -> ENTREGADO
        ENTREGADO, CANCELADO -> null
    }

    companion object {
        fun desde(valor: String?): EstadoPedido = when (valor?.uppercase()) {
            "PENDIENTE", "RECIBIDO" -> RECIBIDO
            "EN_PREPARACION", "PREPARANDO" -> PREPARANDO
            "LISTO" -> LISTO
            "ENTREGADO" -> ENTREGADO
            "CANCELADO" -> CANCELADO
            else -> RECIBIDO
        }
    }
}
