package com.example.android_loop.data.model_dataClass.comentarioResult

import kotlinx.serialization.Serializable



@Serializable
data class CreateComentarioRequest(
    val partner_id: Int,
    val contenido: String,
    val estado: String,
    val valoracion: Float
)

@Serializable
data class CreateComentarioResponse(
    val success: Boolean
)