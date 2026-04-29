package com.example.android_loop.data.repository

import android.util.Log
import com.example.android_loop.data.net.HttpClientProvider
import com.example.android_loop.data.net.Servidor
import com.example.android_loop.data.model_dataClass.usuarioResult.GetUserDataResult
import com.example.android_loop.data.model_dataClass.usuarioResult.GenerarSesionResult
import com.example.android_loop.data.model_dataClass.usuarioResult.ModificarUsuarioResult
import com.example.android_loop.data.model_dataClass.usuarioResult.RegistroResult
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

class UsuarioRepository(private val cliente: HttpClient = HttpClientProvider.cliente) {

    // -- USUARIOS --

    suspend fun generarSesion(username: String, password: String): Result<String> {
        return try {

            val response: RpcResponse<GenerarSesionResult> =
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

            Result.success(response.result.token)

        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun registro(name: String, username: String, email: String, password: String): Result<RegistroResult> {

        return try {
            val response: RpcResponse<RegistroResult> = cliente.post("${Servidor.BASE_URL}/api/v1/loop/register") {
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

            Log.d("DEBUG_REPO_REGISTRO", response.result.success.toString())

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

    suspend fun cambiarCorreo(token: String, nuevoCorreo: String): Result<ModificarUsuarioResult> {
        return try {
            val response: RpcResponse<ModificarUsuarioResult> =
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

    suspend fun cambiarPasswd(token: String, nuevaPasswd: String): Result<ModificarUsuarioResult> {
        return try {
            val response: RpcResponse<ModificarUsuarioResult> =
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

    suspend fun cambiarFotoPerfil(token: String, nuevaFoto: String): Result<ModificarUsuarioResult> {
        return try {
            val response: RpcResponse<ModificarUsuarioResult> =
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

    suspend fun cambiarMobile(token: String, nuevoMobile: String): Result<ModificarUsuarioResult> {
        return try {
            val response: RpcResponse<ModificarUsuarioResult> =
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

    suspend fun cambiarTelephone(token: String, nuevoTel: String): Result<ModificarUsuarioResult> {
        return try {
            val response: RpcResponse<ModificarUsuarioResult> =
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

    suspend fun cambiarIdioma(token: String, nuevoIdioma: String): Result<ModificarUsuarioResult> {
        return try {
            val response: RpcResponse<ModificarUsuarioResult> =
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

    suspend fun borrarCuenta(token: String): Result<ModificarUsuarioResult> {
        return try {
            val response: RpcResponse<ModificarUsuarioResult> =
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

}