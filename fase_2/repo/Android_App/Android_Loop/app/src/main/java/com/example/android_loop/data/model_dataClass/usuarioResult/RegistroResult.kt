package com.example.android_loop.data.model_dataClass.usuarioResult

import kotlinx.serialization.Serializable

@Serializable
data class RegistroResult(

    val success: Boolean

    /**
     * La respuesta devuelve si se pudo realizar un
     * registro de usuario exitosamente (true) o no
     * (false)
     **/
)