package com.example.android_loop.data.repository

import com.example.android_loop.data.model_dataClass.RpcResponse
import com.example.android_loop.data.model_dataClass.denunciaResult.EmitirDenunciaResult
import com.example.android_loop.data.model_dataClass.denunciaResult.GetDenunciaResult
import com.example.android_loop.data.model_dataClass.denunciaResult.RetirarDenunciaResult
import com.example.android_loop.data.net.HttpClientProvider
import com.example.android_loop.data.net.Servidor
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class DenunciaRepository(private val cliente: HttpClient = HttpClientProvider.cliente) {

    suspend fun obtenerDenuncias(token: String): Result<GetDenunciaResult> {
        return try {
            val resp: RpcResponse<GetDenunciaResult> =
                cliente.get("${Servidor.BASE_URL}/api/v1/loop/denuncias") {
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

            return Result.success(resp.result)

        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun emitirDenunciaProducto(token: String, productoId: Int, motivoDenuncia: String): Result<EmitirDenunciaResult> {
        return try {
            val resp: RpcResponse<EmitirDenunciaResult> =
                cliente.post("${Servidor.BASE_URL}/api/v1/loop/denuncias") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        buildJsonObject {
                            put("jsonrpc", "2.0")
                            put("method", "call")
                            put("params", buildJsonObject {
                                put("data", buildJsonObject {
                                    put("producto_id", productoId)
                                    put("motivo_denuncia", motivoDenuncia)
                                })
                            })
                        }
                    )
                }.body()
            return Result.success(resp.result)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun emitirDenunciaComentario(token: String, comentarioId: Int, motivoDenuncia: String): Result<EmitirDenunciaResult> {
        return try {
            val resp: RpcResponse<EmitirDenunciaResult> =
                cliente.post("${Servidor.BASE_URL}/api/v1/loop/denuncias") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        buildJsonObject {
                            put("jsonrpc", "2.0")
                            put("method", "call")
                            put("params", buildJsonObject {
                                put("data", buildJsonObject {
                                    put("comentario_id", comentarioId)
                                    put("motivo_denuncia", motivoDenuncia)
                                })
                            })
                        }
                    )
                }.body()
            return Result.success(resp.result)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun retirarDenuncia(token: String, denunciaId: Int): Result<RetirarDenunciaResult> {
        return try {
            val resp: RpcResponse<RetirarDenunciaResult> =
                cliente.patch("${Servidor.BASE_URL}/api/v1/loop/denuncias") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        buildJsonObject {
                            put("jsonrpc", "2.0")
                            put("method", "call")
                            put("params", buildJsonObject {
                                put("data", buildJsonObject {
                                    put("denuncia_id", denunciaId)
                                })
                            })
                        }
                    )
                }.body()

            return Result.success(resp.result)

        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

}