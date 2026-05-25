package com.example.android_loop.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_loop.data.model_dataClass.denunciaResult.Denuncia
import com.example.android_loop.data.repository.DenunciaRepository
import kotlinx.coroutines.launch

class DenunciaViewModel(private val repo: DenunciaRepository = DenunciaRepository()): ViewModel() {

    var obtenerDenunciaState by mutableStateOf<DenunciaUiState>(DenunciaUiState.Idle)
    var emitirDenunciaState by mutableStateOf<DenunciaUiState>(DenunciaUiState.Idle)
    var retirarDenunciaState by mutableStateOf<DenunciaUiState>(DenunciaUiState.Idle)

    fun obtenerDenuncias(token: String) {
        viewModelScope.launch {
            obtenerDenunciaState = DenunciaUiState.Loading

            val result = repo.obtenerDenuncias(token)

            obtenerDenunciaState = result.fold(
                onSuccess = { DenunciaUiState.SuccessObtenerDenuncias(it.denuncias) },
                onFailure = { DenunciaUiState.Error(it.message ?: "No fue posible obtener las denuncias") }
            )
        }
    }

    fun emitirDenunciaProducto(token: String, productoId: Int, motivoDenuncia: String) {
        viewModelScope.launch {
            emitirDenunciaState = DenunciaUiState.Loading

            val result = repo.emitirDenunciaProducto(token, productoId, motivoDenuncia)

            emitirDenunciaState = result.fold(
                onSuccess = { DenunciaUiState.SuccessEmitirDenunciaProducto(true) },
                onFailure = { DenunciaUiState.Error(it.message ?: "Hubo un error al emitir la denuncia") }
            )
        }
    }

    fun emitirDenunciaComentario(token: String, comentarioId: Int, motivoDenuncia: String) {
        viewModelScope.launch {
            emitirDenunciaState = DenunciaUiState.Loading

            val result = repo.emitirDenunciaComentario(token, comentarioId, motivoDenuncia)

            emitirDenunciaState = result.fold(
                onSuccess = { DenunciaUiState.SuccessEmitirDenunciaComentario(true) },
                onFailure = { DenunciaUiState.Error(it.message ?: "Hubo un error al emitir la denuncia") }
            )
        }
    }

    fun retirarDenuncia(token: String, denunciaId: Int) {
        viewModelScope.launch {
            retirarDenunciaState = DenunciaUiState.Loading

            val result = repo.retirarDenuncia(token, denunciaId)

            retirarDenunciaState = result.fold(
                onSuccess = { DenunciaUiState.SuccessRetirarDenuncia(true) },
                onFailure = { DenunciaUiState.Error(it.message ?: "Hubo un error al retirar la denuncia") }
            )
        }
    }

}

sealed class DenunciaUiState {
    object Idle: DenunciaUiState()
    object Loading: DenunciaUiState()
    data class SuccessObtenerDenuncias(val denuncias: List<Denuncia>): DenunciaUiState()
    data class SuccessEmitirDenunciaProducto(val result: Boolean): DenunciaUiState()
    data class SuccessEmitirDenunciaComentario(val result: Boolean): DenunciaUiState()
    data class SuccessRetirarDenuncia(val result: Boolean): DenunciaUiState()
    data class Error(val message: String): DenunciaUiState()
}