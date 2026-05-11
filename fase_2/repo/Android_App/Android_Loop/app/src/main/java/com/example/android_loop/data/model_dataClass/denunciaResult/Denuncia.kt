package com.example.android_loop.data.model_dataClass.denunciaResult

import kotlinx.serialization.Serializable

// producto_id, comentario_id y comentario son nullables (?) porque no se sabe si se
// denuncia un producto o un comentario, pero tiene que ser uno de ellos
// estado_moderacion, empleado_id, fecha_revision y resultado_moderacion serán nullables
// porque la denuncia pasará por una moderación sólo después de ser emitida

@Serializable
data class Denuncia(
    val denunciante_id: Int,
    val producto_id: Int?,
    val comentario_id: Int?,
    val comentario: String?,
    val usuario_denuncido_id: Int,
    val fecha_emision: String,
    val motivo_denuncia: String,
    val estado_moderacion: String?, // pendiente, revisada, cerrada (Mostrarlo en una lista)
    val estado_usuario: String, // estará activa siempre, en otro endpoint el usuario podrá retirarla
    val resultado_moderacion: String?, // válida, no_valida, duplicada
    val empleado_id: Int?,
    val fecha_revision: String?
)