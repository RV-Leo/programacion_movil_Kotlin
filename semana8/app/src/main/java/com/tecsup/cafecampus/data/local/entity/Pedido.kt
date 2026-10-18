package com.tecsup.cafecampus.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.tecsup.cafecampus.model.EstadoPedido

@Entity(
    tableName = "pedidos",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["clienteId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("clienteId")]
)
data class Pedido(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val clienteId: Int,
    val fecha: Long = System.currentTimeMillis(),
    val estado: String = EstadoPedido.PENDIENTE.name,
    val total: Double,
    val horaRecojo: String,
    val pago: String,
    val nota: String = ""
)
