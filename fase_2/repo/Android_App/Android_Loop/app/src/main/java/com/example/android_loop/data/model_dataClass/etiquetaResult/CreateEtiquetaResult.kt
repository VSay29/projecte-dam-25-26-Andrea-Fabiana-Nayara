package com.example.android_loop.data.model_dataClass.etiquetaResult

import kotlinx.serialization.Serializable

@Serializable
data class CreateEtiquetaRequest(
    val name: String,
    val active: Boolean = true
)

@Serializable
data class CreateEtiquetaResponse(
    val success: Boolean,
    val etiqueta_id: Int,
    val error: String
)