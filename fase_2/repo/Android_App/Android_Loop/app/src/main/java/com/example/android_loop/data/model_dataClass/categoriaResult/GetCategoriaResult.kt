package com.example.android_loop.data.model_dataClass.categoriaResult

import kotlinx.serialization.Serializable

@Serializable
data class CategoriasResponse(
    val success: Boolean,
    val categorias: List<Categoria>
)

@Serializable
data class Categoria(
    val id: Int,
    val nombre: String
)