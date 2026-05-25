package com.example.android_loop.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_loop.data.model_dataClass.compraResult.Compra
import com.example.android_loop.data.repository.CompraRepository
import kotlinx.coroutines.launch

class CompraViewModel(private val repo: CompraRepository = CompraRepository()) : ViewModel() {

    var comprarState by mutableStateOf<ComprasUiState>(ComprasUiState.Idle)
    var obtenerComprasState by mutableStateOf<ComprasUiState>(ComprasUiState.Idle)
    var obtenerVentasState by mutableStateOf<ComprasUiState>(ComprasUiState.Idle)
    var cambiarEstadoState by mutableStateOf<ComprasUiState>(ComprasUiState.Idle)

    suspend fun realizarCompra(token: String, productoId: Int, vendedorId: Int, compradorId: Int): Boolean {
        comprarState = ComprasUiState.Loading

        val result = repo.realizarCompra(token, productoId, vendedorId, compradorId)

        return result.fold(
            onSuccess = {
                comprarState = ComprasUiState.SuccessRealizarCompra(true)
                true
            },
            onFailure = {
                comprarState = ComprasUiState.Error(it.message ?: "No fue posible realizar la compra")
                false
            }
        )
    }

    fun obtenerCompras(token: String) {
        viewModelScope.launch {
            obtenerComprasState = ComprasUiState.Loading

            val result = repo.obtenerCompras(token)

            obtenerComprasState = result.fold(
                onSuccess = { ComprasUiState.SuccessObtenerCompras(it.compras) },
                onFailure = { ComprasUiState.Error(it.message ?: "No fue posible obtener los productos comprados") }
            )
        }
    }

    fun obtenerVentas(token: String) {
        viewModelScope.launch {
            obtenerVentasState = ComprasUiState.Loading

            val result = repo.obtenerVentas(token)

            obtenerVentasState = result.fold(
                onSuccess = { ComprasUiState.SuccessObtenerVentas(it.ventas) },
                onFailure = { ComprasUiState.Error(it.message ?: "No fue posible obtener los productos vendidos") }
            )
        }
    }

    fun cambiarEstadoCompra(token: String, estado: String, compraId: Int) {
        viewModelScope.launch {
            cambiarEstadoState = ComprasUiState.Loading

            val result = repo.cambiarEstado(token, estado, compraId)

            comprarState = result.fold(
                onSuccess = { ComprasUiState.SuccessCambiarEstado(true) },
                onFailure = { ComprasUiState.Error(it.message ?: "No fue posible cambiar el estado de la compra") }
            )
        }
    }

}

sealed class ComprasUiState {
    object Idle: ComprasUiState()
    object Loading: ComprasUiState()
    data class SuccessRealizarCompra(val result: Boolean): ComprasUiState()
    data class SuccessObtenerCompras(val result: List<Compra>): ComprasUiState()
    data class SuccessObtenerVentas(val result: List<Compra>): ComprasUiState()
    data class SuccessCambiarEstado(val result: Boolean): ComprasUiState()
    data class Error(val message: String): ComprasUiState()
}