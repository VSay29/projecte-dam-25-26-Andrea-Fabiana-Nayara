package com.example.android_loop.Producto

data class CreateProductRequest(
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val estado: String,
    val ubicacion: String,
    val antiguedad: String,
    val categoria_id: Int,
    val imagenes: List<ImageRequest>
)

data class ImageRequest(
    val imagen: String,
    val is_principal: Boolean,
    val sequence: Int
)

data class CreateProductResponse(
    val ok: Boolean,
    val product_id: Int
)