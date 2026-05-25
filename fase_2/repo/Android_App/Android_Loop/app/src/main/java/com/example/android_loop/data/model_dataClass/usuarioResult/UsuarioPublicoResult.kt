package com.example.android_loop.data.model_dataClass.usuarioResult

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioPublicoResult(
    val id: Int,
    val nombre: String,
    val imagen: String
)
