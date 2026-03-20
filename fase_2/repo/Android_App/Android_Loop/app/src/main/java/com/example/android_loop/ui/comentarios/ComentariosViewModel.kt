//package com.example.android_loop.ui.comentarios
//
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.android_loop.data.Producto.accesoApi.ApiProductLoop
//import kotlinx.coroutines.launch
//
//class ComentariosViewModel : ViewModel() {
//
//    private val api = ApiProductLoop()
//
//    var comentarios by mutableStateOf<List<Comentario>>(emptyList())
//        private set
//    var isLoading by mutableStateOf(false)
//        private set
//    var errorMessage by mutableStateOf<String?>(null)
//        private set
//    var comentarioEnviado by mutableStateOf(false)
//        private set
//
//    fun cargarComentarios(productId: Int) {
//        viewModelScope.launch {
//            isLoading = true
//            errorMessage = null
//            api.getComentarios(productId)
//                .onSuccess { comentarios = it }
//                .onFailure { errorMessage = it.message }
//            isLoading = false
//        }
//    }
//
//    fun enviarComentario(productoId: Int, contenido: String) {
//        if (contenido.isBlank()) return
//        viewModelScope.launch {
//            isLoading = true
//            errorMessage = null
//            api.crearComentario(CreateComentarioRequest(productoId, contenido))
//                .onSuccess {
//                    comentarioEnviado = true
//                    cargarComentarios(productoId)
//                }
//                .onFailure { errorMessage = it.message }
//            isLoading = false
//        }
//    }
//
//    fun resetComentarioEnviado() {
//        comentarioEnviado = false
//    }
//}