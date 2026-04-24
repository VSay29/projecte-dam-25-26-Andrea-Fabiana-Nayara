package com.example.android_loop.data.model_dataClass.etiquetaResult

import kotlinx.serialization.Serializable

@Serializable
data class GetEtiquetaResponse(
    val success: Boolean,
    val etiquetas: List<Etiqueta>
)

@Serializable
data class Etiqueta(
    val id: Int,
    val name: String,
    val active: Boolean
)