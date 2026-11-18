package com.tecsup.cafecampus.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ajustes")

class PreferenciasStore(private val context: Context) {

    companion object {
        private val KEY_PERFIL_ACTIVO = stringPreferencesKey("perfil_activo")
        private val KEY_MODO_OSCURO = booleanPreferencesKey("modo_oscuro")
        private val KEY_RECIBIR_PEDIDOS = booleanPreferencesKey("recibir_pedidos")
    }

    // Lectura (Flows)
    val perfilActivo: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_PERFIL_ACTIVO] ?: ""
    }

    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_MODO_OSCURO] ?: false
    }

    val recibirPedidos: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_RECIBIR_PEDIDOS] ?: true
    }

    // Escritura (suspend)
    suspend fun guardarPerfil(rol: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_PERFIL_ACTIVO] = rol
        }
    }

    suspend fun guardarModoOscuro(b: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_MODO_OSCURO] = b
        }
    }

    suspend fun guardarRecibirPedidos(b: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_RECIBIR_PEDIDOS] = b
        }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit { preferences ->
            preferences[KEY_PERFIL_ACTIVO] = ""
        }
    }
}
