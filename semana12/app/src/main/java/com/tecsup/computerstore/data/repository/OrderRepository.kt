package com.tecsup.computerstore.data.repository

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class OrderRecord(
    val id: String = "",
    val userId: String = "",
    val customer: String = "",
    val email: String = "",
    val address: String = "",
    val items: String = "",
    val total: Double = 0.0,
    val status: String = "Pendiente",
    val createdAt: Long = 0L
)

class OrderRepository(context: Context) {
    private val db = FirebaseApp.getApps(context.applicationContext).firstOrNull()?.let { FirebaseFirestore.getInstance(it) }
    private val orders get() = db?.collection("pedidos") ?: error("Firestore no está configurado.")

    suspend fun create(uid: String, name: String, email: String, address: String, lines: List<Pair<String, Int>>, total: Double): String {
        require(lines.isNotEmpty()) { "El carrito está vacío." }
        val ref = orders.document()
        val order = OrderRecord(
            id = ref.id, userId = uid, customer = name, email = email, address = address,
            items = lines.joinToString("\n") { "${it.second} x ${it.first}" },
            total = total, createdAt = System.currentTimeMillis()
        )
        ref.set(order).await()
        return ref.id
    }

    suspend fun listForUser(uid: String): List<OrderRecord> = orders.whereEqualTo("userId", uid)
        .get().await().documents.mapNotNull { it.toObject(OrderRecord::class.java) }.sortedByDescending { it.createdAt }

    suspend fun listAll(): List<OrderRecord> = orders.get().await().documents
        .mapNotNull { it.toObject(OrderRecord::class.java) }.sortedByDescending { it.createdAt }

    suspend fun updateStatus(id: String, status: String) { orders.document(id).update("status", status).await() }
}
