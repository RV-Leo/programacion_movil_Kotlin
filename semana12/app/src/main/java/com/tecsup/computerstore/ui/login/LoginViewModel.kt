package com.tecsup.computerstore.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.computerstore.AppContainer
import com.tecsup.computerstore.domain.model.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val correo: String = "",
    val clave: String = "",
    val cargando: Boolean = false,
    val error: String? = null
)

class LoginViewModel(private val appContainer: AppContainer) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _sesionIniciada = MutableStateFlow<Sesion?>(null)
    val sesionIniciada: StateFlow<Sesion?> = _sesionIniciada.asStateFlow()

    fun cambiarCorreo(value: String) { _uiState.value = _uiState.value.copy(correo = value, error = null) }
    fun cambiarClave(value: String) { _uiState.value = _uiState.value.copy(clave = value, error = null) }

    fun iniciarSesion() {
        val datos = _uiState.value
        when {
            datos.correo.isBlank() -> _uiState.value = datos.copy(error = "Ingresa tu correo electrónico.")
            !datos.correo.contains('@') -> _uiState.value = datos.copy(error = "Escribe un correo válido.")
            datos.clave.isBlank() -> _uiState.value = datos.copy(error = "Ingresa tu contraseña.")
            else -> viewModelScope.launch {
                _uiState.value = datos.copy(cargando = true, error = null)
                appContainer.authRepository.login(datos.correo.trim(), datos.clave)
                    .onSuccess { _sesionIniciada.value = it }
                    .onFailure { _uiState.value = datos.copy(error = mensajeError(it)) }
                _uiState.value = _uiState.value.copy(cargando = false)
            }
        }
    }

    private fun mensajeError(error: Throwable): String {
        val detalle = error.message.orEmpty()
        return when {
            "google-services.json" in detalle -> "Configura Firebase agregando google-services.json en app/."
            "PERMISSION_DENIED" in detalle -> "No se pudo leer el perfil. Revisa las reglas de Firestore."
            "No existe el perfil" in detalle -> detalle
            else -> "No se pudo iniciar sesión. Verifica el correo y la contraseña."
        }
    }

    companion object {
        fun Factory(appContainer: AppContainer): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = LoginViewModel(appContainer) as T
        }
    }
}
