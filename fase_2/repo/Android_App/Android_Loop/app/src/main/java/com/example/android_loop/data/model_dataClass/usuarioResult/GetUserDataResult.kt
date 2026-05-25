package com.example.android_loop.data.model_dataClass.usuarioResult

import kotlinx.serialization.Serializable

@Serializable
data class GetUserDataResult (

    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String?,
    val mobile: String?,
    val idioma: String,
    val image_1920: String

    /**
     * Datos del usuario. Se obtienen al navegar al
     * perfil de usuario.
     **/

)