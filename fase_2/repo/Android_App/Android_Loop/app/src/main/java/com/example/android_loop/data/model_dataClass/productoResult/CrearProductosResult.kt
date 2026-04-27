package com.example.android_loop.data.model_dataClass.productoResult

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement


/**
 * Respuesta del servidor al crear un producto
 **/

@Serializable
data class CreateProductResponse(
    val ok: Boolean,
    val product_id: Int
)

@Serializable
data class CreateProductRpcResponse(
    val jsonrpc: String,
    val id: Int? = null,
    val result: CreateProductResponse? = null,
    val error: JsonElement? = null
)

/**
 * Requests de los datos necesarios para crear el producto
 * (Equivalente a lo que sería un modelo "Producto"
 **/

@Serializable
data class CreateProductRequest(
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val estado: String,
    val ubicacion: String,
    val antiguedad: String,
    val categoria_id: Int,
    val etiqueta_ids: List<Int>,
    val imagenes: List<ImageRequest>
)

@Serializable
data class ImageRequest(
    val imagen: String,
    val is_principal: Boolean,
    val sequence: Int
)