package com.tecsup.computerstore.data.auth

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tecsup.computerstore.domain.model.Rol
import com.tecsup.computerstore.domain.model.Sesion
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(context: Context) : AuthRepository {
    private val firebaseApp = FirebaseApp.getApps(context.applicationContext).firstOrNull()
    private val auth = firebaseApp?.let { FirebaseAuth.getInstance(it) }
    private val firestore = firebaseApp?.let { FirebaseFirestore.getInstance(it) }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getSesionActual(): Flow<Sesion?> = callbackFlow {
        val currentAuth = auth
        if (currentAuth == null) {
            trySend(null)
            close()
            return@callbackFlow
        }

        val listener = FirebaseAuth.AuthStateListener { updatedAuth ->
            val user = updatedAuth.currentUser
            if (user == null) {
                trySend(null)
            } else {
                launch {
                    runCatching { leerSesion(user.uid, user.email.orEmpty()) }
                        .onSuccess { trySend(it) }
                        .onFailure { trySend(null) }
                }
            }
        }
        currentAuth.addAuthStateListener(listener)
        awaitClose { currentAuth.removeAuthStateListener(listener) }
    }

    override suspend fun login(email: String, clave: String): Result<Sesion> {
        val currentAuth = auth
            ?: return Result.failure(IllegalStateException("Falta configurar Firebase: coloca google-services.json en app/."))
        return try {
            val user = currentAuth.signInWithEmailAndPassword(email, clave).await().user
                ?: throw IllegalStateException("Firebase no devolvió el usuario autenticado.")
            Result.success(leerSesion(user.uid, user.email ?: email))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        auth?.signOut()
    }

    private suspend fun leerSesion(uid: String, email: String): Sesion {
        val database = firestore ?: throw IllegalStateException("Firestore no está configurado.")
        val snapshot = database.collection("usuarios").document(uid).get().await()
        if (!snapshot.exists()) throw IllegalStateException("No existe el perfil de este usuario en Firestore.")
        val rol = if (snapshot.getString("rol").equals("admin", ignoreCase = true)) Rol.ADMIN else Rol.USUARIO
        val nombre = snapshot.getString("nombre")?.takeIf { it.isNotBlank() } ?: email.substringBefore('@')
        return Sesion(uid = uid, email = email, usuario = nombre, rol = rol)
    }
}
