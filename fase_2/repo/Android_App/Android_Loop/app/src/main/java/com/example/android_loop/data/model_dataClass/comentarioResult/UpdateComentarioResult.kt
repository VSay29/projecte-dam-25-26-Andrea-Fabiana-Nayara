package com.example.android_loop.data.model_dataClass.comentarioResult

import kotlinx.serialization.Serializable

@Serializable
data class UpdateComentarioRequest(
    val contenido: String,
    val estado: String,
    val valoracion: Float
)

@Serializable
data class UpdateComentarioResponse(
    val success: Boolean = true
)