package com.example.android_loop.data.model_dataClass.denunciaResult

import kotlinx.serialization.Serializable

@Serializable
data class GetDenunciaResult(
    val denuncias: List<Denuncia> = emptyList()
)