package com.example.android_loop.data.model_dataClass.productoResult

import com.example.android_loop.data.model_dataClass.categoriaResult.Categoria
import com.example.android_loop.data.model_dataClass.etiquetaResult.Etiqueta
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductosResult(
    val ok: Boolean,
    val count: Int,
    val products: List<Producto>
)

@Serializable
data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val estado: String,
    val ubicacion: String,
    val antiguedad: String?,
    val categoria: Categoria?,
    val propietario: Propietario?,
    val etiquetas: List<Etiqueta>,
    val imagenes: List<Imagen>,
    val thumbnail: String? = null
)

@Serializable
data class Imagen(
    val id: Int,
    val principal: Boolean,
    val orden: Int
)

@Serializable
data class ImagenDetalle(
    val id: Int,
    val imagen: String,
    @SerialName("is_principal")
    val principal: Boolean,
    val sequence: Int
)

@Serializable
data class ImagenesProductoResponse(
    val imagenes: List<ImagenDetalle>
)

@Serializable
data class Propietario(
    val id: Int,
    val nombre: String
)
