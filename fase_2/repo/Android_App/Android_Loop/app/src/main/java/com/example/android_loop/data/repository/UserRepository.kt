package com.example.android_loop.data.repository

import com.example.android_loop.data.accesoApi.ApiLoop
import com.example.android_loop.data.model_dataClass.AddFavoritosResult
import com.example.android_loop.data.model_dataClass.GetFavoritosResult
import com.example.android_loop.data.model_dataClass.GetUserDataResult
import com.example.android_loop.data.model_dataClass.PatchUsuarioResult
import com.example.android_loop.data.model_dataClass.RegistroResult
import com.example.android_loop.data.model_dataClass.RemoveFavoritoResult

class UserRepository(private val api: ApiLoop = ApiLoop()) {

    // -- USUARIOS --

    suspend fun login(username: String, password: String): Result<String> {
        return api.login(username, password).map { it.token }
    }

    suspend fun registro(name: String, username: String, email: String, password: String): Result<RegistroResult> {
        return api.registro(name, username, email, password)
    }

    suspend fun getUserData(token: String): Result<GetUserDataResult> {
        return api.getUserData(token)
    }

    suspend fun cambiarCorreo(token: String, nuevoCorreo: String): Result<PatchUsuarioResult> {
        return api.cambiarCorreo(token, nuevoCorreo)
    }

    suspend fun cambiarPasswd(token: String, nuevoPasswd: String): Result<PatchUsuarioResult> {
        return api.cambiarPasswd(token, nuevoPasswd)
    }

    suspend fun cambiarFotoPerfil(token: String, nuevaFoto: String): Result<PatchUsuarioResult> {
        return api.cambiarFotoPerfil(token, nuevaFoto)
    }

    suspend fun cambiarMobile(token: String, nuevoMobile: String): Result<PatchUsuarioResult> {
        return api.cambiarMobile(token, nuevoMobile)
    }

    suspend fun cambiarTelephone(token: String, nuevoTel: String): Result<PatchUsuarioResult> {
        return api.cambiarTelephone(token, nuevoTel)
    }

    suspend fun cambiarIdioma(token: String, nuevoIdioma: String): Result<PatchUsuarioResult> {
        return api.cambiarIdioma(token, nuevoIdioma)
    }

    suspend fun borrarCuenta(token: String): Result<PatchUsuarioResult> {
        return api.borrarCuenta(token)
    }

    // -- FAVORITOS --

    suspend fun addFavoritos(token: String, productoId: Int): Result<AddFavoritosResult> {
        return api.addFavoritos(token, productoId)
    }

    suspend fun removeFavorito(token: String, productoId: Int): Result<RemoveFavoritoResult> {
        return api.removeFavorito(token, productoId)
    }

    suspend fun getFavoritos(token: String): Result<GetFavoritosResult> {
        return api.getUserFavoritos(token)
    }

}