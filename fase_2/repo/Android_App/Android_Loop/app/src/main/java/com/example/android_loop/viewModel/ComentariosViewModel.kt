package com.example.android_loop.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_loop.data.model_dataClass.comentarioResult.Comentario
import com.example.android_loop.data.model_dataClass.comentarioResult.CreateComentarioRequest
import com.example.android_loop.data.model_dataClass.comentarioResult.UpdateComentarioRequest
import com.example.android_loop.data.repository.ComentarioRepository
import com.example.android_loop.data.repository.ProductoRepository
import com.example.android_loop.data.repository.UsuarioRepository
import kotlinx.coroutines.launch

class ComentariosViewModel(private val productoRepo: ProductoRepository = ProductoRepository(), private val userRepo: UsuarioRepository = UsuarioRepository(), private val comentarioRepo: ComentarioRepository = ComentarioRepository()) : ViewModel() {

    var comentarioUiState by mutableStateOf<ComentarioUiState>(ComentarioUiState.Idle)

    var comentarioEnviado by mutableStateOf(false)

    var currentUserName by mutableStateOf("")
    var currentUserId by mutableStateOf(0)

    fun cargarUsuarioActual(token: String) {
        viewModelScope.launch {
            userRepo.getUserData(token)
            .onSuccess {
                currentUserName = it.name
                currentUserId = it.id
            }
        }
    }

    fun cargarComentarios(token: String, productId: Int) {
        viewModelScope.launch {
            comentarioUiState = ComentarioUiState.Loading

            val result = comentarioRepo.getComentarios(token, productId)

            comentarioUiState = result.fold(
                onSuccess = { ComentarioUiState.SuccessCargarComentarios(it) },
                onFailure = { ComentarioUiState.Error(it.message ?: "No se ha podido cargar las reseñas") }
            )

        }
    }

    // DOC: Esta función y las de editar y eliminar, sólo se llamarán con los perfiles de otros usuarios, no el propietario

    fun enviarComentario(token: String, usuarioId: Int, contenido: String, valoracion: Float) {
        if (contenido.isBlank()) return
        viewModelScope.launch {

            comentarioUiState = ComentarioUiState.Loading

            val result = comentarioRepo.crearComentario(token, CreateComentarioRequest(usuarioId, contenido, "published", valoracion))

            comentarioUiState = result.fold(
                onSuccess = { ComentarioUiState.SuccessEnviarComentario(true) },
                onFailure = { ComentarioUiState.Error(it.message ?: "No se ha podido crear la reseña") }
            )
        }
    }

    fun resetComentarioEnviado() {
        comentarioEnviado = false
    }

    fun editarComentario(token: String, comentarioId: Int, contenido: String, valoracion: Float, usuarioId: Int) {
        if (contenido.isBlank()) return
        viewModelScope.launch {

            comentarioUiState = ComentarioUiState.Loading

            val result = comentarioRepo.editarComentario(token, comentarioId, UpdateComentarioRequest(contenido, "published", valoracion))

            comentarioUiState = result.fold(
                onSuccess = { ComentarioUiState.SuccessEditarComentario(true) },
                onFailure = { ComentarioUiState.Error(it.message ?: "No se ha podido editar la reseña") }
            )
        }
    }

    fun eliminarComentario(token: String, comentarioId: Int, usuarioId: Int) {
        viewModelScope.launch {

            comentarioUiState = ComentarioUiState.Loading

            val result = comentarioRepo.eliminarComentario(token, comentarioId)

            comentarioUiState = result.fold(
                onSuccess = { ComentarioUiState.SuccessEliminarComentario(true) },
                onFailure = { ComentarioUiState.Error(it.message ?: "No se ha podido eliminar la reseña") }
            )

        }
    }
}

sealed class ComentarioUiState {

    object Idle : ComentarioUiState()

    object Loading: ComentarioUiState()

    data class SuccessCargarComentarios(val resp: List<Comentario>): ComentarioUiState()

    data class SuccessEnviarComentario(val resp: Boolean): ComentarioUiState()

    data class SuccessEditarComentario(val resp: Boolean): ComentarioUiState()

    data class SuccessEliminarComentario(val resp: Boolean): ComentarioUiState()

    data class Error(val message: String): ComentarioUiState()

}
