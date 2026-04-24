package com.example.android_loop.data.repository

import com.example.android_loop.data.net.HttpClientProvider
import com.example.android_loop.data.net.Servidor
import com.example.android_loop.data.model_dataClass.RpcResponse
import com.example.android_loop.data.model_dataClass.comentarioResult.Comentario
import com.example.android_loop.data.model_dataClass.comentarioResult.GetComentariosResponse
import com.example.android_loop.data.model_dataClass.comentarioResult.CreateComentarioRequest
import com.example.android_loop.data.model_dataClass.comentarioResult.CreateComentarioResponse
import com.example.android_loop.data.model_dataClass.comentarioResult.UpdateComentarioRequest
import com.example.android_loop.data.model_dataClass.comentarioResult.UpdateComentarioResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class ComentarioRepository(private val cliente: HttpClient = HttpClientProvider.cliente) {

    // GET COMENTARIOS

    suspend fun getComentarios(token: String, productId: Int): Result<List<Comentario>> {
        return try {

            val response: RpcResponse<GetComentariosResponse> =
                cliente.get("${Servidor.BASE_URL}/api/v1/loop/usuarios/$productId/comentarios") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        buildJsonObject {
                            put("jsonrpc", "2.0")
                            put("method", "call")
                            put("params", buildJsonObject {})
                        }
                    )
                }.body()

            Result.success(response.result.comentarios)

        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    // CREAR COMENTARIOS

    suspend fun crearComentario(token: String, request: CreateComentarioRequest): Result<CreateComentarioResponse> {

        return try {

            val response: RpcResponse<CreateComentarioResponse> =
                cliente.post("${Servidor.BASE_URL}/api/v1/loop/comentarios") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        buildJsonObject {
                            put("jsonrpc", "2.0")
                            put("method", "call")
                            put("params", buildJsonObject {
                                put("data", buildJsonObject {
                                    put("contenido", request.contenido)
                                    put("estado", request.estado)
                                    put("valoracion", request.valoracion)
                                    put("partner_id", request.partner_id)
                                })
                            })
                        }
                    )
                }.body()

            Result.success(response.result)

        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    // EDITAR COMENTARIOS

    suspend fun editarComentario(token: String, comentarioId: Int, request: UpdateComentarioRequest): Result<UpdateComentarioResponse> {

        return try {


            val response: RpcResponse<UpdateComentarioResponse> =
                cliente.patch("${Servidor.BASE_URL}/api/v1/loop/comentarios/$comentarioId") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        buildJsonObject {
                            put("jsonrpc", "2.0")
                            put("method", "call")
                            put("params", buildJsonObject {
                                put("data", buildJsonObject {
                                    put("estado", request.contenido)
                                    put("contenido", request.contenido)
                                    put("valoracion", request.valoracion)
                                })
                            })
                        }
                    )
                }.body()

            Result.success(response.result)

        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    // ELIMINAR COMENTARIOS

    suspend fun eliminarComentario(token: String, comentarioId: Int): Result<UpdateComentarioResponse> {

        return try {

            val response: RpcResponse<UpdateComentarioResponse> =
                cliente.delete("${Servidor.BASE_URL}/api/v1/loop/comentarios/$comentarioId") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        buildJsonObject {
                            put("jsonrpc", "2.0")
                            put("method", "call")
                            put("params", buildJsonObject {})
                        }
                    )
                }.body()

            Result.success(response.result)

        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

}