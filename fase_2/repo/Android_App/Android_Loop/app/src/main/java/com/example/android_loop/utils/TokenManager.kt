package com.example.android_loop.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import com.auth0.jwt.JWT
import com.example.android_loop.utils.encriptacionConfig.desencriptarToken
import com.example.android_loop.utils.encriptacionConfig.encriptarToken
import java.util.Date

fun getToken(context: Context): String {
    val prefs = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)
    val cifrado = prefs.getString("token", "") ?: ""
    if (cifrado.isEmpty()) return ""

    return desencriptarToken(cifrado)
}

fun setToken(context: Context, token: String) {
    val prefs = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)
    if (token.isEmpty()) {
        prefs.edit { putString("token", "").commit() }
        return
    }
    val tokenCifrado = encriptarToken(token)
    prefs.edit { putString("token", tokenCifrado).commit() }
}

// SE TOMA EL ID DEL TOKEN DEL USUARIO PARA QUE CADA CARRITO SEA ASOCIADO A SU USUARIO CORRESPONDIENTE
fun getUserIdFromToken(token: String): Int? {
    return try {
        JWT.decode(token).getClaim("uid").asInt()
    } catch (_: Exception) {
        0
    }
}

fun tokenValido(token: String): Boolean {
    if (token.isEmpty()) return false

    return try {
        val jwt = JWT.decode(token)

        val expiresAt = jwt.expiresAt
        val issuer = jwt.issuer

        val isNotExpired = expiresAt.after(Date())
        val isCorrectIssuer = issuer == "AdminLoop"

        isNotExpired && isCorrectIssuer
    } catch (_: Exception) {
        false
    }
}