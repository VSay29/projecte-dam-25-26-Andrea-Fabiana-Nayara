package com.example.android_loop.data.model_dataClass.favoritoResult

import kotlinx.serialization.Serializable

@Serializable
data class GetFavoritosResult (
    val result: List<Favorito>
)

@Serializable
data class Favorito(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val ubicacion: String,
    val imagenes: List<String>
)

/**
 * El GET de favoritos devuelve los productos marcados,
 * por lo que se ha construido un modelo data class Favorito
 * con los datos de esos productos y se devuelven todos
 * después en una lista (GetFavoritosResult)
 **/