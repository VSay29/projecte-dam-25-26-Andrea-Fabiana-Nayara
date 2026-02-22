package com.example.android_loop.Producto

data class ProductResponse(
    val ok: Boolean,
    val count: Int,
    val products: List<Product>
)
