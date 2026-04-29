package com.example.android_loop.data.repository

import com.example.android_loop.data.net.HttpClientProvider
import com.example.android_loop.data.net.Servidor
import com.example.android_loop.data.model_dataClass.RpcResponse
import com.example.android_loop.data.model_dataClass.etiquetaResult.CreateEtiquetaRequest
import com.example.android_loop.data.model_dataClass.etiquetaResult.CreateEtiquetaResponse
import com.example.android_loop.data.model_dataClass.etiquetaResult.Etiqueta
import com.example.android_loop.data.model_dataClass.etiquetaResult.GetEtiquetaResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class EtiquetaRepository(private val cliente: HttpClient = HttpClientProvider.cliente) {

    // CREATE ETIQUETA

    suspend fun createEtiqueta(token: String, request: CreateEtiquetaRequest): Result<CreateEtiquetaResponse> {

        return try {
            val response: RpcResponse<CreateEtiquetaResponse> =
                cliente.post("${Servidor.BASE_URL}/api/v1/loop/etiquetas") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        buildJsonObject {
                            put("jsonrpc", "2.0")
                            put("method", "call")
                            put("params", buildJsonObject {
                                put("data", buildJsonObject {
                                    put("name", request.name)
                                    put("active", request.active)
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

    // GET ETIQUETAS

    suspend fun getEtiquetas(token: String,): Result<List<Etiqueta>> {
        return try {

            val response: GetEtiquetaResponse =
                cliente.get("${Servidor.BASE_URL}/api/v1/loop/etiquetas") {
                    header("Authorization", "Bearer $token")
                    accept(ContentType.Application.Json)
                }.body()

            Result.success(response.etiquetas)

        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

}