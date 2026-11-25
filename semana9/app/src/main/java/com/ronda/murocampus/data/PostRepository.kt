package com.ronda.murocampus.data

import com.ronda.murocampus.data.remote.PostApi
import com.ronda.murocampus.model.Comentario
import com.ronda.murocampus.model.NuevoPost
import com.ronda.murocampus.model.Post
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import retrofit2.Response

interface PostRepository {
    suspend fun listar(): Result<List<Post>>
    suspend fun detalle(id: Int): Result<Post>
    suspend fun comentarios(id: Int): Result<List<Comentario>>
    suspend fun porUsuario(userId: Int): Result<List<Post>>
    suspend fun crear(nuevo: NuevoPost): Result<Post>
    suspend fun eliminar(id: Int): Result<Unit>
}

class PostRepositoryRemote(private val api: PostApi) : PostRepository {
    private suspend fun <T> llamar(bloque: suspend () -> T): Result<T> = try {
        Result.success(bloque())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun listar() = llamar { api.listar() }
    override suspend fun detalle(id: Int) = llamar { api.detalle(id) }
    override suspend fun comentarios(id: Int) = llamar { api.comentarios(id) }
    override suspend fun porUsuario(userId: Int) = llamar { api.porUsuario(userId) }
    override suspend fun crear(nuevo: NuevoPost) = llamar { api.crear(nuevo) }

    override suspend fun eliminar(id: Int): Result<Unit> = llamar {
        val respuesta: Response<Unit> = api.eliminar(id)
        if (!respuesta.isSuccessful) throw HttpException(respuesta)
        Unit
    }
}
