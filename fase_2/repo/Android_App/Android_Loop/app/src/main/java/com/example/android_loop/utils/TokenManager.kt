package com.example.android_loop.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.android_loop.data.repository.UsuarioRepository

fun getToken(context: Context): String {
    val prefs = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)
    val token = prefs.getString("token", "") ?: ""
    return token
}

// TODO: Falta encriptar el Token

fun setToken(context: Context, token: String, username: String, passwd: String) {
    val prefs = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)
    prefs.edit {
        putString("token", token)
        putString("username", username)
        putString("passwd", passwd)
    }
}

// SE TOMA EL ID DEL TOKEN DEL USUARIO PARA QUE CADA CARRITO SEA ASOCIADO A SU USUARIO CORRESPONDIENTE
fun getUserIdFromToken(token: String): Int? {
    return try {
        JWT.decode(token).getClaim("uid").asInt()
    } catch (e: Exception) {
        null
    }
}

fun tokenExpirado(token: String): Boolean {

    val repoUser = UsuarioRepository()

    if (token.isEmpty() || token == "") return false

    return try {
        val clave = "1*/GvDCk_]ni`H8M164(t=€j(FD}3L-~k2c<LoPE+(uxTo+.R"
        val algoritmo = Algorithm.HMAC256(clave)

        val verificador = JWT.require(algoritmo)
            .withIssuer("AdminLoop")
            .build()
        verificador.verify(token)
        true
    } catch (e: Exception) {
        return false
    }
}

