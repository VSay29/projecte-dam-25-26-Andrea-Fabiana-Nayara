package com.example.android_loop.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import com.auth0.jwt.JWT
import java.util.Date

fun getToken(context: Context): String {
    val prefs = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)
    val token = prefs.getString("token", "") ?: ""
    return token
}

// TODO: Falta encriptar el Token

fun setToken(context: Context, token: String) {
    val prefs = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)
    prefs.edit { putString("token", token) }
}

// TODO: FALTA REGENERAR TOKEN AL CADUCAR

// SE TOMA EL ID DEL TOKEN DEL USUARIO PARA QUE CADA CARRITO SEA ASOCIADO A SU USUARIO CORRESPONDIENTE
fun getUserIdFromToken(token: String): Int? {
    return try {
        JWT.decode(token).getClaim("uid").asInt()
    } catch (e: Exception) {
        null
    }
}

fun tokenValido(token: String): Boolean {

    if (token.isEmpty() || token == "") return false

    return try {
        val tokenDecodificado = JWT.decode(token)
        val exp = tokenDecodificado.expiresAt ?: return false
        exp.after(Date())
    } catch (e: Exception) {
        return false
    }
}

