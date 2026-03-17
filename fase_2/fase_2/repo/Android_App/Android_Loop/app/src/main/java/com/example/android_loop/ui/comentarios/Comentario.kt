package com.example.android_loop.ui.comentarios

import kotlinx.serialization.Serializable

@Serializable
data class Comentario(
    val id: Int,
    val contenido: String,
    val fecha_creacion: String,
    val comentador: String,
    val estado: String,
    val moderador: String? = null,
    val fecha_moderacion: String? = null
)