package com.tecsup.computerstore.ui

import com.tecsup.computerstore.data.repository.ItemRepository
import com.tecsup.computerstore.domain.model.Item
import com.tecsup.computerstore.notification.Notificador
import com.tecsup.computerstore.ui.lista.ListaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val testItems = listOf(
        Item(1, "Laptop Gamer", "Laptop potente", 4500.0, "laptop", "", 10, false),
        Item(2, "Teclado Mecánico", "RGB Switch Blue", 250.0, "periferico", "", 15, true)
    )

    private val fakeRepository = FakeItemRepository(testItems)
    private val fakeNotificador = FakeNotificador()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cargarProductos cambia estado a Exito cuando hay datos`() = runTest {
        val viewModel = ListaViewModel(fakeRepository, fakeNotificador)
        testDispatcher.scheduler.advanceUntilIdle()

        val estado = viewModel.uiState.value
        assertTrue(estado is UiState.Exito)
        assertEquals(2, (estado as UiState.Exito).datos.size)
    }

    @Test
    fun `cambiarFavorito alterna el estado de favorito`() = runTest {
        val viewModel = ListaViewModel(fakeRepository, fakeNotificador)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.cambiarFavorito(1)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(fakeRepository.favoritoCambiado)
    }
}

private class FakeItemRepository(private val items: List<Item>) : ItemRepository {
    var favoritoCambiado = false
    private val itemsFlow = MutableStateFlow(items)

    override fun getItems(): Flow<List<Item>> = itemsFlow
    override fun getFavoritos(): Flow<List<Item>> = flowOf(items.filter { it.esFavorito })
    override suspend fun getItemById(id: Int): Item? = items.find { it.id == id }
    override suspend fun sincronizarConRemoto(): Int = items.size
    override suspend fun cambiarFavorito(id: Int, esFavorito: Boolean) {
        favoritoCambiado = true
    }
    override suspend fun agregarItem(item: Item) {}
    override suspend fun eliminarItem(id: Int) {}
    override suspend fun cantidadGuardada(): Int = items.size
    override suspend fun cantidadFavoritos(): Int = items.count { it.esFavorito }
    override suspend fun limpiarCache() {}
}

private class FakeNotificador : Notificador {
    val notificaciones = mutableListOf<Pair<String, String>>()
    override fun mostrarNotificacion(titulo: String, mensaje: String) {
        notificaciones.add(titulo to mensaje)
    }
}
