package com.tecsup.computerstore.ui.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.computerstore.AppContainer
import com.tecsup.computerstore.domain.model.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegistroUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val cargando: Boolean = false,
    val error: String? = null
)

class RegistroViewModel(private val appContainer: AppContainer) : ViewModel() {
    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    private val _sesionIniciada = MutableStateFlow<Sesion?>(null)
    val sesionIniciada: StateFlow<Sesion?> = _sesionIniciada.asStateFlow()

    fun cambiarNombre(value: String) { _uiState.value = _uiState.value.copy(nombre = value, error = null) }
    fun cambiarCorreo(value: String) { _uiState.value = _uiState.value.copy(correo = value, error = null) }
    fun cambiarClave(value: String) { _uiState.value = _uiState.value.copy(clave = value, error = null) }

    fun registrar() {
        val datos = _uiState.value
        when {
            datos.nombre.isBlank() ->
                _uiState.value = datos.copy(error = "Ingresa tu nombre completo.")
            datos.correo.isBlank() ->
                _uiState.value = datos.copy(error = "Ingresa tu correo electrónico.")
            !datos.correo.contains('@') ->
                _uiState.value = datos.copy(error = "Escribe un correo electrónico válido.")
            datos.clave.isBlank() ->
                _uiState.value = datos.copy(error = "Ingresa tu contraseña.")
            datos.clave.length < 6 ->
                _uiState.value = datos.copy(error = "La contraseña debe tener al menos 6 caracteres.")
            else -> viewModelScope.launch {
                _uiState.value = datos.copy(cargando = true, error = null)
                try {
                    appContainer.authRepository.registrar(datos.nombre.trim(), datos.correo.trim(), datos.clave)
                        .onSuccess { _sesionIniciada.value = it }
                        .onFailure { _uiState.value = _uiState.value.copy(error = mensajeError(it)) }
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(error = mensajeError(e))
                } finally {
                    _uiState.value = _uiState.value.copy(cargando = false)
                }
            }
        }
    }

    private fun mensajeError(error: Throwable): String {
        val detalle = error.message.orEmpty()
        return when {
            "google-services.json" in detalle -> "Configura Firebase agregando google-services.json en app/."
            "The email address is already in use" in detalle || "already exists" in detalle ->
                "El correo electrónico ya está registrado. Intenta iniciar sesión."
            "PERMISSION_DENIED" in detalle || "Missing or insufficient permissions" in detalle ->
                "Firebase creó la cuenta, pero Firestore rechazó el perfil. Revisa las reglas de la colección usuarios. Puedes volver a intentar con el mismo correo."
            "tardó demasiado" in detalle -> detalle
            else -> "No se pudo crear la cuenta: ${error.localizedMessage ?: "Error desconocido"}"
        }
    }

    companion object {
        fun Factory(appContainer: AppContainer): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = RegistroViewModel(appContainer) as T
        }
    }
}
