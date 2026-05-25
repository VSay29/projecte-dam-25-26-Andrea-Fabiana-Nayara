package com.example.android_loop.data.model_dataClass.denunciaResult

import kotlinx.serialization.Serializable

@Serializable
data class Denuncia(
    val id: Int = 0,
    val denunciante_id: Int = 0,
    val producto_id: Int? = null,
    val producto_nombre: String? = null,
    val comentario_id: Int? = null,
    val comentario_texto: String? = null,
    val usuario_denunciado_id: Int = 0,
    val usuario_denunciado_nombre: String? = null,
    val fecha_emision: String = "",
    val motivo_denuncia: String = "",
    val estado_moderacion: String? = null,
    val estado_usuario: String = "",
    val resultado_moderacion: String? = null,
    val empleado_id: Int? = null,
    val fecha_revision: String? = null
)
