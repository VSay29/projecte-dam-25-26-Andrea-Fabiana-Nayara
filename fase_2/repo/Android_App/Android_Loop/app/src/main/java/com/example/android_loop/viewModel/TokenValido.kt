package com.example.android_loop.viewModel

import android.content.Context
import com.example.android_loop.data.repository.UsuarioRepository
import com.example.android_loop.utils.encriptarPasswd
import com.example.android_loop.utils.getToken
import com.example.android_loop.utils.setToken
import com.example.android_loop.utils.tokenExpirado

class GetTokenValidoUseCase(private val repo: UsuarioRepository = UsuarioRepository(), private val context: Context) {

    suspend operator fun invoke(): String {
        val token = getToken(context)

        if (tokenExpirado(token)) return token

        val prefs = context.getSharedPreferences("loop_prefs", Context.MODE_PRIVATE)
        val username = prefs.getString("username", "") ?: ""
        val passwd = prefs.getString("passwd", "") ?: ""

        if (username.isNotEmpty() && passwd.isNotEmpty()) {
            val result = repo.generarSesion(username, encriptarPasswd(passwd))
            if (result.isSuccess) {
                val nuevoToken = result.getOrNull() ?: ""
                setToken(context, nuevoToken, username, encriptarPasswd(passwd))
                return nuevoToken
            }
        }

        return ""
    }

}