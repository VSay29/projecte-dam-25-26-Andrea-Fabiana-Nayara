package com.example.android_loop.Producto

data class CreateEtiquetaRequest(
    val data: EtiquetaData
)

data class EtiquetaData(
    val name: String,
    val active: Boolean = true
)

data class CreateEtiquetaResponse(
    val success: Boolean?,
    val etiqueta_id: Int?,
    val error: String?
)
