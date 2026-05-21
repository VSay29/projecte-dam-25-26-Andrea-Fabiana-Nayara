package com.example.android_loop.data.model_dataClass.productoResult

import com.example.android_loop.data.model_dataClass.etiquetaResult.Etiqueta
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProductResponse(
    val ok: Boolean
)

// TODO: Si el usuario no le cambia el valor al producto,
// TODO: permanece con los mismos valores de antes

@Serializable
data class UpdateProductRequest(
    val nombre: String?,
    val descripcion: String?,
    val precio: Double?,
    val estado: String?,
    val categoria_id: Int?,
    val etiquetas: List<Etiqueta>?,
    val imagenes: List<ImageRequest>?
)