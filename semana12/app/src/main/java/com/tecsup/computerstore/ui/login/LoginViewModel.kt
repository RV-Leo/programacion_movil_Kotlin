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
    fun usarCuentaAdmin() {
        _uiState.value = _uiState.value.copy(correo = "admin@admin.com", clave = "admin123456", error = null)
    }
    fun limpiarCredencialesAdmin() {
        _uiState.value = _uiState.value.copy(correo = "", clave = "", error = null)
    }

    fun iniciarSesion(accesoAdmin: Boolean = false) {
        val datos = _uiState.value
        when {
            datos.correo.isBlank() -> _uiState.value = datos.copy(error = "Ingresa tu correo electrónico.")
            !datos.correo.contains('@') -> _uiState.value = datos.copy(error = "Escribe un correo válido.")
            datos.clave.isBlank() -> _uiState.value = datos.copy(error = "Ingresa tu contraseña.")
            else -> viewModelScope.launch {
                _uiState.value = datos.copy(cargando = true, error = null)
                try {
                    appContainer.authRepository.login(datos.correo.trim(), datos.clave)
                        .onSuccess { sesion ->
                            val rolEsperadoAdmin = sesion.rol == com.tecsup.computerstore.domain.model.Rol.ADMIN
                            if (rolEsperadoAdmin == accesoAdmin) _sesionIniciada.value = sesion
                            else {
                                appContainer.authRepository.logout()
                                _uiState.value = _uiState.value.copy(error = if (accesoAdmin) "La cuenta no tiene rol de administrador." else "Esta cuenta es de administrador. Cambia al acceso Administrador.")
                            }
                        }
                        .onFailure { _uiState.value = _uiState.value.copy(error = mensajeError(it)) }
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
