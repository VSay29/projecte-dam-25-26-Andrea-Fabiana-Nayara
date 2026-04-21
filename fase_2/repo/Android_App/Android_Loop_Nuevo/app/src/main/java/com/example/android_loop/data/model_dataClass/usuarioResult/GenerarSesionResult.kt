package com.example.android_loop.data.model_dataClass.usuarioResult

import kotlinx.serialization.Serializable

@Serializable
data class GenerarSesionResult (

    val token: String

    /**
     * Esto es el resultado del login, pero se le ha nombrado
     * GenerarSesion ya que nos devuelve el token necesario
     * para iniciar sesión, y en caso de caducar el mismo, se
     * vuelva a generar uno nuevo con esta función sin liarnos
     * con los nombres
     **/

)