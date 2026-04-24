package com.example.android_loop.data.model_dataClass.usuarioResult

import kotlinx.serialization.Serializable

@Serializable
data class ModificarUsuarioResult (

    val success: Boolean

    /**
     * Esta respuesta trabaja tanto para modificar datos del
     * usuario, como para borrar la cuenta del usuario.
     **/

)