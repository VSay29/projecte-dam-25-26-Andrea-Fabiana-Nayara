package com.example.android_loop.data.model_dataClass.compraResult

import kotlinx.serialization.Serializable

@Serializable
data class Compra(
    val compra_id: Int,
    val producto: ProductoComprado,
    val vendedor_id: Int,
    val comprador_id: Int,
    val state: String
)

@Serializable
data class ProductoComprado(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val ubicacion: String,
    val imagenes: List<String>
)