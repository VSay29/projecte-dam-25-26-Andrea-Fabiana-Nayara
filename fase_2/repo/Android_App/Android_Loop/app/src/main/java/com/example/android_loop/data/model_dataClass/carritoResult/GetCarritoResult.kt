package com.example.android_loop.data.model_dataClass.carritoResult

import kotlinx.serialization.Serializable

@Serializable
data class GetCarritoResult(
    val productos: List<ProductoCarrito>
)

@Serializable
data class ProductoCarrito(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val ubicacion: String,
    val imagenes: List<String>
)
