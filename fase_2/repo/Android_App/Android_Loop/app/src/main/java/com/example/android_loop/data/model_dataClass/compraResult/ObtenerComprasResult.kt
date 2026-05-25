package com.example.android_loop.data.model_dataClass.compraResult

import kotlinx.serialization.Serializable

@Serializable
data class ObtenerComprasResult(
    val success: Boolean,
    val compras: List<Compra>
)