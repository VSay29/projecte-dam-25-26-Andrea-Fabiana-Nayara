package com.example.android_loop.data.repository

import com.example.android_loop.data.net.HttpClientProvider
import com.example.android_loop.data.net.Servidor
import com.example.android_loop.data.model_dataClass.RpcResponse
import com.example.android_loop.data.model_dataClass.carritoResult.AddCarritoResult
import com.example.android_loop.data.model_dataClass.carritoResult.GetCarritoResult
import com.example.android_loop.data.model_dataClass.carritoResult.RemoveCarritoResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class CarritoApiRepository(private val cliente: HttpClient = HttpClientProvider.cliente) {

    suspend fun getCarrito(token: String): Result<GetCarritoResult> {
        return try {
            val response: RpcResponse<GetCarritoResult> =
                cliente.get("${Servidor.BASE_URL}/api/v1/loop/carrito") {
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

    suspend fun addCarrito(token: String, productoId: Int): Result<AddCarritoResult> {
        return try {
            val response: RpcResponse<AddCarritoResult> =
                cliente.post("${Servidor.BASE_URL}/api/v1/loop/carrito/add") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        buildJsonObject {
                            put("jsonrpc", "2.0")
                            put("method", "call")
                            put("params", buildJsonObject {
                                put("data", buildJsonObject {
                                    put("producto_id", productoId)
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

    suspend fun removeCarrito(token: String, productoId: Int): Result<RemoveCarritoResult> {
        return try {
            val response: RpcResponse<RemoveCarritoResult> =
                cliente.post("${Servidor.BASE_URL}/api/v1/loop/carrito/remove") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        buildJsonObject {
                            put("jsonrpc", "2.0")
                            put("method", "call")
                            put("params", buildJsonObject {
                                put("data", buildJsonObject {
                                    put("producto_id", productoId)
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
}
