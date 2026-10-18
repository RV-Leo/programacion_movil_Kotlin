package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.entity.Usuario
import com.tecsup.cafecampus.data.prefs.PreferenciasStore
import com.tecsup.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repo: CafeRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _sesionInicializada = MutableStateFlow(false)
    val sesionInicializada: StateFlow<Boolean> = _sesionInicializada.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val perfilActivo: StateFlow<String> = prefs.perfilActivo
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val recibirPedidos: StateFlow<Boolean> = prefs.recibirPedidos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    init {
        viewModelScope.launch {
            val perfilGuardado = perfilActivo.first()
            if (perfilGuardado.isNotBlank()) {
                _usuarioActual.value = repo.usuarioPorRol(perfilGuardado)
            }
            _sesionInicializada.value = true
        }
    }

    fun guardarModoOscuro(activado: Boolean) {
        viewModelScope.launch { prefs.guardarModoOscuro(activado) }
    }

    fun guardarRecibirPedidos(activado: Boolean) {
        viewModelScope.launch { prefs.guardarRecibirPedidos(activado) }
    }

    fun cambiarPerfil(onCambiar: (String) -> Unit) {
        viewModelScope.launch {
            val siguiente = if (perfilActivo.value.equals("ADMIN", ignoreCase = true)) "CLIENTE" else "ADMIN"
            prefs.guardarPerfil(siguiente)
            _usuarioActual.value = repo.usuarioPorRol(siguiente)
            onCambiar(siguiente)
        }
    }

    fun login(correo: String, clave: String, rolSeleccionado: String, onSuccess: (String) -> Unit) {
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Por favor ingrese correo y contraseña"
            return
        }

        viewModelScope.launch {
            val usuario = repo.login(correo.trim(), clave.trim())
            if (usuario == null) {
                _error.value = "Correo o contraseña incorrectos"
            } else if (!usuario.rol.equals(rolSeleccionado, ignoreCase = true)) {
                _error.value = "El usuario no corresponde al rol seleccionado"
            } else {
                _usuarioActual.value = usuario
                _error.value = null
                prefs.guardarPerfil(usuario.rol)
                onSuccess(usuario.rol)
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            prefs.cerrarSesion()
            _usuarioActual.value = null
        }
    }
}
