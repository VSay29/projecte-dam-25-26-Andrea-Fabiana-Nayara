package com.example.android_loop.utils.navegacionConfig

object ROUTES {

    // Rutas sin parametrizar

    const val LOGIN = "login"
    const val HOME = "home"
    const val REGISTRO = "registro"
    const val PERFIL_USUARIO = "perfil_Usuario"
    const val FAVORITOS = "favoritos"
    const val DENUNCIAS = "denuncias"
    const val CARRITO = "carrito"
    const val AJUSTES = "ajustes"

    // Rutas parametrizadas

    const val DETALLE_PRODUCTO = "ver_producto"
    const val DETALLE_PRODUCTO_ID = "$DETALLE_PRODUCTO/{productId}"
    const val PERFIL_VENDEDOR = "perfil_Vendedor"
    const val PERFIL_VENDEDOR_PARAMS = "$PERFIL_VENDEDOR/{vendedorId}/{vendedorNombre}"

    const val CREAR_PRODUCTO = "crear_producto"

    const val CREAR_PRODUCTO_PARAM = "$CREAR_PRODUCTO/{productoId}"

    const val COMPRA = "compra"

    const val COMPRA_PARAM = "$COMPRA/{productos}"
}