package com.example.android_loop.data.repository

import com.example.android_loop.data.model_dataClass.RpcResponse
import com.example.android_loop.data.model_dataClass.compraResult.CambiarEstadoCompraResult
import com.example.android_loop.data.model_dataClass.compraResult.ObtenerComprasResult
import com.example.android_loop.data.model_dataClass.compraResult.ObtenerVentasResult
import com.example.android_loop.data.model_dataClass.compraResult.RealizarCompraResult
import com.example.android_loop.data.net.HttpClientProvider
import com.example.android_loop.data.net.Servidor
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class CompraRepository(private val cliente: HttpClient = HttpClientProvider.cliente) {

    suspend fun realizarCompra(token: String, productoId: Int, vendedorId: Int): Result<RealizarCompraResult> {
        return try {
            val resp: RpcResponse<RealizarCompraResult> =
                cliente.post("${Servidor.BASE_URL}/api/v1/loop/compra/comprar") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        buildJsonObject {
                            put("jsonrpc", "2.0")
                            put("method", "call")
                            put("params", buildJsonObject {
                                put("data", buildJsonObject {
                                    put("producto_id", productoId)
                                    put("vendedor_id", vendedorId)
                                    put("state", "vendido")
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

    suspend fun obtenerCompras(token: String): Result<ObtenerComprasResult> {
        return try {
            val resp: RpcResponse<ObtenerComprasResult> =
                cliente.post("${Servidor.BASE_URL}/api/v1/loop/compras/obtener") {
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

    suspend fun obtenerVentas(token: String): Result<ObtenerVentasResult> {
        return try {
            val resp: RpcResponse<ObtenerVentasResult> =
                cliente.post("${Servidor.BASE_URL}/api/v1/loop/ventas/obtener") {
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

    suspend fun cambiarEstado(token: String, state: String, compraId: Int): Result<CambiarEstadoCompraResult> {
        return try {
            val resp: RpcResponse<CambiarEstadoCompraResult> =
                cliente.post("${Servidor.BASE_URL}/api/v1/loop/cambiarEstado") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        buildJsonObject {
                            put("jsonrpc", "2.0")
                            put("method", "call")
                            put("params", buildJsonObject {
                                put("data", buildJsonObject {
                                    put("compra_id", compraId)
                                    put("state", state)
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