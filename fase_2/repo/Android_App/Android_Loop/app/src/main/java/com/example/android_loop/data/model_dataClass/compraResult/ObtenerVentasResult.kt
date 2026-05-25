package com.example.android_loop.data.model_dataClass.compraResult

import kotlinx.serialization.Serializable

@Serializable
data class ObtenerVentasResult(
    val success: Boolean,
    val ventas: List<Compra>
)