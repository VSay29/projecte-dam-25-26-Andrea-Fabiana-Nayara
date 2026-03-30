package com.example.android_loop.data.accesoApi

import com.example.android_loop.data.model_dataClass.AddFavoritosResult
import com.example.android_loop.data.model_dataClass.GetFavoritosResult
import com.example.android_loop.data.model_dataClass.GetUserDataResult
import com.example.android_loop.data.model_dataClass.LoginResult
import com.example.android_loop.data.model_dataClass.PatchUsuarioResult
import com.example.android_loop.data.model_dataClass.RegistroResult
import com.example.android_loop.data.model_dataClass.RemoveFavoritoResult
import com.example.android_loop.data.model_dataClass.RpcResponse
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

class ApiLoop(private val cliente: HttpClient = HttpClientProvider.cliente) {

    // -- USUARIOS --

    suspend fun login(username: String, password: String): Result<LoginResult> {
        return try {

            val response: RpcResponse<LoginResult> =
                cliente.post("${Servidor.BASE_URL}/api/v1/loop/auth") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        buildJsonObject {
                            put("jsonrpc", "2.0")
                            put("method", "call")
                            put("params", buildJsonObject {
                                put("username", username)
                                put("password", password)
                            })
                        }
                    )
                }.body()

            Result.success(response.result)

        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun registro(name: String, username: String, email: String, password: String): Result<RegistroResult> {

        return try {
            val response: RpcResponse<RegistroResult> =
                cliente.post("${Servidor.BASE_URL}/api/v1/loop/register") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        buildJsonObject {
                            put("jsonrpc", "2.0")
                            put("method", "call")
                            put("params", buildJsonObject {
                                put("data", buildJsonObject {
                                    put("name", name)
                                    put("username", username)
                                    put("email", email)
                                    put("password", password)
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

    suspend fun getUserData(token: String): Result<GetUserDataResult> {
        return try {
            val response: RpcResponse<GetUserDataResult> =
                cliente.get( "${Servidor.BASE_URL}/api/v1/loop/me" ) {
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

    // -- MODIFICACIONES DE LA PANTALLA AJUSTES --

    suspend fun cambiarCorreo(token: String, nuevoCorreo: String): Result<PatchUsuarioResult> {
        return try {
            val response: RpcResponse<PatchUsuarioResult> =
                cliente.patch ("${Servidor.BASE_URL}/api/v1/loop/me") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        buildJsonObject {
                            put("jsonrpc", "2.0")
                            put("method", "call")
                            put("params", buildJsonObject {
                                put("data", buildJsonObject {
                                    put("email", nuevoCorreo)
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

    suspend fun cambiarPasswd(token: String, nuevaPasswd: String): Result<PatchUsuarioResult> {
        return try {
            val response: RpcResponse<PatchUsuarioResult> =
                cliente.patch("${Servidor.BASE_URL}/api/v1/loop/me") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        buildJsonObject {
                            put("jsonrpc", "2.0")
                            put("method", "call")
                            put("params", buildJsonObject {
                                put("data", buildJsonObject {
                                    put("password", nuevaPasswd)
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

    suspend fun cambiarFotoPerfil(token: String, nuevaFoto: String): Result<PatchUsuarioResult> {
        return try {
            val response: RpcResponse<PatchUsuarioResult> =
                cliente.patch("${Servidor.BASE_URL}/api/v1/loop/me") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        buildJsonObject {
                            put("jsonrpc", "2.0")
                            put("method", "call")
                            put("params", buildJsonObject {
                                put("data", buildJsonObject {
                                    put("image_1920", nuevaFoto)
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

    suspend fun cambiarMobile(token: String, nuevoMobile: String): Result<PatchUsuarioResult> {
        return try {
            val response: RpcResponse<PatchUsuarioResult> =
                cliente.patch("${Servidor.BASE_URL}/api/v1/loop/me") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        buildJsonObject {
                            put("jsonrpc", "2.0")
                            put("method", "call")
                            put("params", buildJsonObject {
                                put("data", buildJsonObject {
                                    put("mobile", nuevoMobile)
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

    suspend fun cambiarTelephone(token: String, nuevoTel: String): Result<PatchUsuarioResult> {
        return try {
            val response: RpcResponse<PatchUsuarioResult> =
                cliente.patch("${Servidor.BASE_URL}/api/v1/loop/me") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        buildJsonObject {
                            put("jsonrpc", "2.0")
                            put("method", "call")
                            put("params", buildJsonObject {
                                put("data", buildJsonObject {
                                    put("phone", nuevoTel)
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

    suspend fun cambiarIdioma(token: String, nuevoIdioma: String): Result<PatchUsuarioResult> {
        return try {
            val response: RpcResponse<PatchUsuarioResult> =
                cliente.patch("${Servidor.BASE_URL}/api/v1/loop/me") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        buildJsonObject {
                            put("jsonrpc", "2.0")
                            put("method", "call")
                            put("params", buildJsonObject {
                                put("data", buildJsonObject {
                                    put("idioma", nuevoIdioma)
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

    // -- ELIMINAR LA CUENTA --

    suspend fun borrarCuenta(token: String): Result<PatchUsuarioResult> {
        return try {
            val response: RpcResponse<PatchUsuarioResult> =
                cliente.delete("${Servidor.BASE_URL}/api/v1/loop/me") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        buildJsonObject {}
                    )
                }.body()
            Result.success(response.result)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    // -- FAVORITOS --

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