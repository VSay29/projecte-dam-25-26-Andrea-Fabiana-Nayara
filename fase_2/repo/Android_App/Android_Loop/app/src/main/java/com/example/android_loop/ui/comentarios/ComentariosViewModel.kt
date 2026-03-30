package com.example.android_loop.ui.comentarios

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_loop.data.accesoApi.ApiProductoLoop
import com.example.android_loop.data.repository.UserRepository
import com.example.android_loop.ui.comentarios.UpdateComentarioData
import com.example.android_loop.ui.comentarios.UpdateComentarioRequest
import kotlinx.coroutines.launch

class ComentariosViewModel : ViewModel() {

    private val api = ApiProductoLoop()
    private val userRepository = UserRepository()

    var comentarios by mutableStateOf<List<Comentario>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var comentarioEnviado by mutableStateOf(false)
        private set
    var currentUserName by mutableStateOf("")
        private set
    var currentUserId by mutableStateOf(0)
        private set

    fun cargarUsuarioActual(token: String) {
        viewModelScope.launch {
            userRepository.getUserData(token)
                .onSuccess {
                    currentUserName = it.name
                    currentUserId = it.id
                }
        }
    }

    fun cargarComentarios(token: String, productId: Int) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            api.getComentarios(token, productId)
                .onSuccess { comentarios = it }
                .onFailure { errorMessage = it.message }
            isLoading = false
        }
    }

    fun enviarComentario(token: String, usuarioId: Int, contenido: String, valoracion: Float? = null) {
        if (contenido.isBlank()) return
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            api.crearComentario(token, CreateComentarioRequest(data = CreateComentarioData(usuarioId, contenido, "published", valoracion)))
                .onSuccess { response ->
                    if (response.success == true) {
                        comentarioEnviado = true
                        cargarComentarios(token, usuarioId)
                    } else {
                        errorMessage = response.error ?: "Error al enviar el comentario"
                    }
                }
                .onFailure { errorMessage = it.message }
            isLoading = false
        }
    }

    fun resetComentarioEnviado() {
        comentarioEnviado = false
    }

    fun editarComentario(token: String, comentarioId: Int, contenido: String, valoracion: Float?, usuarioId: Int) {
        if (contenido.isBlank()) return
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            api.editarComentario(
                token,
                comentarioId,
                UpdateComentarioRequest(UpdateComentarioData(contenido, "published", valoracion))
            )
                .onSuccess { response ->
                    if (response.success == true) {
                        cargarComentarios(token, usuarioId)
                    } else {
                        errorMessage = response.error ?: "Error al editar el comentario"
                    }
                }
                .onFailure { errorMessage = it.message }
            isLoading = false
        }
    }

    fun eliminarComentario(token: String, comentarioId: Int, usuarioId: Int) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            api.eliminarComentario(token, comentarioId)
                .onSuccess { response ->
                    if (response.success == true) {
                        cargarComentarios(token, usuarioId)
                    } else {
                        errorMessage = response.error ?: "Error al eliminar el comentario"
                    }
                }
                .onFailure { errorMessage = it.message }
            isLoading = false
        }
    }
}
