package com.example.android_loop.ui.comentarios

import kotlinx.serialization.Serializable

@Serializable
data class ComentariosResponse(
    val comentarios: List<Comentario>
)

@Serializable
data class CreateComentarioRequest(
    val producto_id: Int,
    val contenido: String
)

@Serializable
data class CreateComentarioResponse(
    val id: Int
)
