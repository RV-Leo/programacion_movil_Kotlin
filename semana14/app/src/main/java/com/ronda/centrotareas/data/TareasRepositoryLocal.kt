package com.ronda.centrotareas.data

import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.ronda.centrotareas.model.ProgresoPrimos
import kotlinx.coroutines.withContext
import android.util.Log

class TareasRepositoryLocal : TareasRepository {
    override fun contarPrimosConProgreso(hasta: Int): Flow<ProgresoPrimos> = flow {
        var cantidad = 0
        val intervalo = (hasta / 100).coerceAtLeast(1)
        emit(ProgresoPrimos(0f))
        for (n in 2..hasta) {
            kotlinx.coroutines.currentCoroutineContext().ensureActive()
            if (esPrimo(n)) cantidad++
            if (n % intervalo == 0 || n == hasta) {
                val progreso = n.toFloat() / hasta
                emit(ProgresoPrimos(progreso, if (n == hasta) cantidad else null))
            }
        }
    }.flowOn(Dispatchers.Default)

    override fun progresoTarea(): Flow<Float> = flow {
        for (i in 1..100) {
            kotlinx.coroutines.currentCoroutineContext().ensureActive()
            if (i == 1) Log.d("HILO", "Dentro: ${Thread.currentThread().name}")
            delay(40)
            emit(i / 100f)
        }
    }.flowOn(Dispatchers.Default)

    override suspend fun contarPrimos(hasta: Int): Int = withContext(Dispatchers.Default) {
        var cantidad = 0
        for (n in 2..hasta) {
            currentCoroutineContext().ensureActive()
            if (esPrimo(n)) cantidad++
        }
        cantidad
    }

    override suspend fun cargarPerfil(): String {
        delay(2_000)
        return "Ana Torres (ficticia)"
    }

    override suspend fun cargarPedidos(simularError: Boolean): List<String> {
        delay(3_000)
        if (simularError) throw IOException("Sin conexión")
        return listOf("Pedido 101", "Pedido 102", "Pedido 103")
    }

    private fun esPrimo(n: Int): Boolean {
        if (n < 2) return false
        var divisor = 2
        while (divisor.toLong() * divisor <= n) {
            if (n % divisor == 0) return false
            divisor++
        }
        return true
    }
}

