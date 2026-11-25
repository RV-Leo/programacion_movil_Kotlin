package com.ronda.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ronda.murocampus.data.PostRepository
import com.ronda.murocampus.model.Comentario
import com.ronda.murocampus.model.NuevoPost
import com.ronda.murocampus.model.Post
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

sealed interface UiState<out T> {
    data object Cargando : UiState<Nothing>
    data class Exito<T>(val datos: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}

fun mensajeDe(e: Throwable): String = when (e) {
    is IOException -> "Sin conexión a internet.\nRevisa tu red e inténtalo otra vez."
    is HttpException -> "Error del servidor (${e.code()})"
    is SerializationException -> "Respuesta inesperada"
    else -> "Error desconocido"
}

data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

private val tecsupTitles = listOf(
    "Laboratorio de Inteligencia Artificial con Modelos Locales en Tecsup",
    "Proyecto Integrador: Automatización de Celda Robotizada en Mecatrónica",
    "Seminario Avanzado de Ciberseguridad y Protección de Redes Industriales",
    "Desarrollo de Aplicaciones Móviles Nativas con Jetpack Compose y Kotlin",
    "Diseño y Mantenimiento de Sistemas SCADA para la Industria 4.0",
    "Taller Práctico de IoT y Sensores Inteligentes con Arduino y ESP32",
    "Cloud Computing y DevOps: Despliegue de Arquitecturas en AWS",
    "Optimización de Procesos de Manufactura Esbelta (Lean Manufacturing)",
    "Instalación de Fibra Óptica y Conectividad de Alta Velocidad",
    "Gestión de Proyectos Tecnológicos con Metodologías Ágiles Scrum"
)

private val tecsupBodies = listOf(
    "Estudiantes de la carrera de Diseño y Desarrollo de Software están desarrollando modelos predictivos utilizando IA en el campus de Tecsup.",
    "El equipo de Mecatrónica Industrial finalizó con éxito el montaje y programación de los controladores lógicos programables (PLC) para la línea de ensamblaje.",
    "Se convoca a toda la comunidad estudiantil al workshop de hacking ético y mitigación de vulnerabilidades organizado por la facultad de TI.",
    "Aprende las mejores prácticas de arquitectura limpia, estados en Compose y ViewModels reactivos en las sesiones prácticas de este ciclo.",
    "Revisión de normativas técnicas y protocolos industriales para garantizar la eficiencia energética en plantas de procesamiento.",
    "Pruebas de campo con adquisición de datos en tiempo real mediante microcontroladores para optimizar el monitoreo ambiental.",
    "Configuración de contenedores Docker y pipelines de integración continua para proyectos de software de alto rendimiento.",
    "Análisis de casos reales de la industria minera y manufacturera para la reducción de tiempos de ciclo y desperdicios.",
    "Instalación de fibra óptica y certificación de enlaces de red de área local para laboratorios de computación.",
    "Planificación de sprints, tableros Kanban y entrega continua de valor para los proyectos finales de titulación."
)

fun enriquecePost(post: Post): Post {
    val index = kotlin.math.abs(post.id - 1) % tecsupTitles.size
    val userCarrera = when (post.userId) {
        1 -> "Software y IA"
        2 -> "Mecatrónica"
        3 -> "Ciberseguridad"
        4 -> "Redes y Telecom"
        5 -> "Automatización"
        else -> "Tecnología Tecsup"
    }
    return post.copy(
        title = "[${userCarrera}] ${tecsupTitles[index]}",
        body = tecsupBodies[index]
    )
}

class PostsViewModel(private val repo: PostRepository) : ViewModel() {
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista = _lista.asStateFlow()

    private val _busqueda = MutableStateFlow("")
    val busqueda = _busqueda.asStateFlow()

    val listaVisible = combine(_lista, _busqueda) { estado, texto ->
        if (estado is UiState.Exito) {
            UiState.Exito(estado.datos.filter { it.title.contains(texto.trim(), ignoreCase = true) })
        } else {
            estado
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Cargando)

    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro = _usuarioFiltro.asStateFlow()

    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle = _detalle.asStateFlow()

    private val _creacion = MutableStateFlow<UiState<Post>?>(null)
    val creacion = _creacion.asStateFlow()

    private val _eliminacion = MutableStateFlow<UiState<Unit>?>(null)
    val eliminacion = _eliminacion.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _lista.value = UiState.Cargando
            (_usuarioFiltro.value?.let { repo.porUsuario(it) } ?: repo.listar())
                .onSuccess { posts ->
                    val postsTecsup = posts.map { enriquecePost(it) }
                    _lista.value = UiState.Exito(postsTecsup)
                }
                .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        cargar()
    }

    fun buscarTitulo(texto: String) {
        _busqueda.value = texto
    }

    fun limpiarCreacion() {
        _creacion.value = null
    }

    fun publicar(titulo: String, texto: String) {
        if (titulo.isBlank() || texto.isBlank()) return
        viewModelScope.launch {
            _creacion.value = UiState.Cargando
            repo.crear(NuevoPost(userId = 1, title = titulo.trim(), body = texto.trim()))
                .onSuccess { nuevoPost ->
                    val postEnriquecido = enriquecePost(nuevoPost)
                    _creacion.value = UiState.Exito(postEnriquecido)
                }
                .onFailure { _creacion.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun limpiarEliminacion() {
        _eliminacion.value = null
    }

    fun eliminar(id: Int) {
        viewModelScope.launch {
            _eliminacion.value = UiState.Cargando
            repo.eliminar(id)
                .onSuccess {
                    _eliminacion.value = UiState.Exito(Unit)
                    val estadoActual = _lista.value
                    if (estadoActual is UiState.Exito) {
                        _lista.value = UiState.Exito(estadoActual.datos.filterNot { it.id == id })
                    }
                }
                .onFailure { _eliminacion.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun abrir(id: Int) {
        viewModelScope.launch {
            _detalle.value = UiState.Cargando
            try {
                val resultado = coroutineScope {
                    val postRaw = repo.detalle(id).getOrThrow()
                    val post = enriquecePost(postRaw)
                    val comentarios = async { repo.comentarios(id).getOrThrow() }
                    DetalleUi(post, comentarios.await())
                }
                _detalle.value = UiState.Exito(resultado)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _detalle.value = UiState.Error(mensajeDe(e))
            }
        }
    }
}

