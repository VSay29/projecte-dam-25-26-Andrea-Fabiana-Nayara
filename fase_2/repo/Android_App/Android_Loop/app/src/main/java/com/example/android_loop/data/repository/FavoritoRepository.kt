package com.example.android_loop.data.repository

import com.example.android_loop.data.net.HttpClientProvider
import com.example.android_loop.data.net.Servidor
import com.example.android_loop.data.model_dataClass.favoritoResult.AddFavoritosResult
import com.example.android_loop.data.model_dataClass.favoritoResult.GetFavoritosResult
import com.example.android_loop.data.model_dataClass.favoritoResult.RemoveFavoritoResult
import com.example.android_loop.data.model_dataClass.RpcResponse
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

class FavoritoRepository(private val cliente: HttpClient = HttpClientProvider.cliente) {

    suspend fun addFavoritos(token: String, productoId: Int): Result<AddFavoritosResult> {

        return try {

            val response: RpcResponse<AddFavoritosResult> =
                cliente.post("${Servidor.BASE_URL}/api/v1/loop/favoritos/add") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        buildJsonObject {
                            put("jsonrpc", "2.0")
                            put("method", "call")
                            put("params", buildJsonObject {
                                put("data", buildJsonObject {
                                    put("product_id", productoId)
                                })
                            })
                        }
                    )
                }.body()

            return Result.success(response.result)

        } catch (ex: Exception) {
            Result.failure(ex)
        }

    }

    suspend fun removeFavorito(token: String, productoId: Int): Result<RemoveFavoritoResult> {
        return try {

            val response: RpcResponse<RemoveFavoritoResult> =
                cliente.post("${Servidor.BASE_URL}/api/v1/loop/favoritos/remove") {
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

            return Result.success(response.result)

        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun getUserFavoritos(token: String): Result<GetFavoritosResult> {
        return try {

            val response: RpcResponse<GetFavoritosResult> =
                cliente.get("${Servidor.BASE_URL}/api/v1/loop/favoritos") {
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

            return Result.success(response.result)

        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

}