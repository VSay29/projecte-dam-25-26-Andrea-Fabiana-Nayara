package com.example.android_loop.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_loop.data.model_dataClass.productoResult.Producto
import com.example.android_loop.data.model_dataClass.usuarioResult.GetUserDataResult
import com.example.android_loop.data.repository.ProductoRepository
import com.example.android_loop.data.repository.UsuarioRepository
import kotlinx.coroutines.launch

class PerfilViewModel(private val usuarioRepo: UsuarioRepository = UsuarioRepository(), private val productoRepo: ProductoRepository = ProductoRepository()): ViewModel() {

    var perfilUiState by mutableStateOf<PerfilUiState>(PerfilUiState.Idle)

    fun getUserData(token: String) {
        viewModelScope.launch {
            perfilUiState = PerfilUiState.Loading
            val result = usuarioRepo.getUserData(token)

            perfilUiState = result.fold(
                onSuccess = { PerfilUiState.SuccessGetUserData(it) },
                onFailure = { PerfilUiState.Error(it.message ?: "No se pudo actualizar la foto de perfil") }
            )
        }
    }

    fun cambiarFotoPerfil(token: String, foto: String) {
        viewModelScope.launch {
            perfilUiState = PerfilUiState.Loading
            val result = usuarioRepo.cambiarFotoPerfil(token, foto)

            perfilUiState = result.fold(
                onSuccess = {
                    PerfilUiState.SuccessCambiarFotoPerfil(true)
                },
                onFailure = {
                    PerfilUiState.Error(it.message ?: "No se pudo actualizar la foto de perfil")
                }
            )
        }
    }
    
    fun cargarProductosUsuario(token: String, idUsuario: Int) {
        viewModelScope.launch {

            perfilUiState = PerfilUiState.Loading
            val result = productoRepo.getProductos(token)

            perfilUiState = result.fold(
                onSuccess = { it -> PerfilUiState.SuccessCargarProductoUsuario(it.products.filter { it.propietario?.id == idUsuario }) },
                onFailure = { PerfilUiState.Error(it.message ?: "No se pudo cargar los productos del usuario") }
            )
        }
    }

}

sealed class PerfilUiState {

    object Idle : PerfilUiState()

    object Loading: PerfilUiState()

    data class SuccessGetUserData(val resp: GetUserDataResult): PerfilUiState()

    data class SuccessCambiarFotoPerfil(val resp: Boolean): PerfilUiState()

    data class SuccessCargarProductoUsuario(val resp: List<Producto>): PerfilUiState()

    data class Error(val message: String): PerfilUiState()

}