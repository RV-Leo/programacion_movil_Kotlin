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
import kotlinx.coroutines.withTimeout

class FirebaseAuthRepository(context: Context) : AuthRepository {
    private companion object {
        const val FIREBASE_TIMEOUT_MS = 20_000L
        const val ADMIN_EMAIL = "admin@admin.com"
        const val ADMIN_PASSWORD = "admin123456"
    }

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
            val normalizedEmail = email.trim().lowercase()
            val bootstrapAdmin = normalizedEmail == ADMIN_EMAIL && clave == ADMIN_PASSWORD
            val user = runCatching {
                withTimeout(FIREBASE_TIMEOUT_MS) {
                    currentAuth.signInWithEmailAndPassword(normalizedEmail, clave).await().user
                }
            }.getOrElse { signInError ->
                if (!bootstrapAdmin) throw signInError
                withTimeout(FIREBASE_TIMEOUT_MS) {
                    currentAuth.createUserWithEmailAndPassword(ADMIN_EMAIL, ADMIN_PASSWORD).await().user
                }
            }
                ?: throw IllegalStateException("Firebase no devolvió el usuario autenticado.")
            if (bootstrapAdmin) {
                val database = firestore ?: throw IllegalStateException("Firestore no está configurado.")
                withTimeout(FIREBASE_TIMEOUT_MS) {
                    database.collection("usuarios").document(user.uid)
                        .set(mapOf("nombre" to "admin", "rol" to "admin")).await()
                }
            }
            Result.success(leerSesion(user.uid, user.email ?: normalizedEmail))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registrar(nombre: String, email: String, clave: String): Result<Sesion> {
        val currentAuth = auth
            ?: return Result.failure(IllegalStateException("Falta configurar Firebase: coloca google-services.json en app/."))
        val database = firestore
            ?: return Result.failure(IllegalStateException("Firestore no está configurado."))
        return try {
            // Reutiliza la cuenta si Auth la creó pero el guardado del perfil falló.
            val normalizedEmail = email.trim()
            if (normalizedEmail.equals(ADMIN_EMAIL, ignoreCase = true)) {
                throw IllegalArgumentException("Esta cuenta está reservada para el acceso de administrador.")
            }
            val user = currentAuth.currentUser
                ?.takeIf { it.email.equals(normalizedEmail, ignoreCase = true) }
                ?: withTimeout(FIREBASE_TIMEOUT_MS) {
                    currentAuth.createUserWithEmailAndPassword(normalizedEmail, clave).await().user
                }
                ?: throw IllegalStateException("Firebase no creó el usuario.")
            val datosUsuario = mapOf(
                "nombre" to nombre.ifBlank { normalizedEmail.substringBefore('@') },
                "rol" to "usuario"
            )
            withTimeout(FIREBASE_TIMEOUT_MS) {
                database.collection("usuarios").document(user.uid).set(datosUsuario).await()
            }
            val sesion = Sesion(uid = user.uid, email = normalizedEmail, usuario = nombre, rol = Rol.USUARIO)
            Result.success(sesion)
        } catch (e: kotlinx.coroutines.TimeoutCancellationException) {
            Result.failure(IllegalStateException("La conexión con Firebase tardó demasiado. Revisa tu internet y las reglas de Firestore; puedes volver a intentar guardar el perfil.", e))
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
