package com.example.android_loop.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_loop.data.model_dataClass.productoResult.ImagenDetalle
import com.example.android_loop.data.model_dataClass.productoResult.Producto
import com.example.android_loop.data.repository.ProductoRepository
import kotlinx.coroutines.launch

class VerProductoViewModel(private val productoRepo: ProductoRepository = ProductoRepository()): ViewModel() {

    var verProductoUiState by mutableStateOf<VerProductoUiState>(VerProductoUiState.Idle)
    var imagenesUiState by mutableStateOf<List<ImagenDetalle>>(emptyList())

    fun cargarProducto(token: String, productoId: Int) {

        viewModelScope.launch {

            verProductoUiState = VerProductoUiState.Loading
            val result = productoRepo.getProductos(token)

            verProductoUiState = result.fold(
                onSuccess = { it -> VerProductoUiState.SuccessCargarProducto(it.products.filter { it.id == productoId }[0]) },
                onFailure = { VerProductoUiState.Error(it.message ?: "El producto no está disponible en estos momentos") }
            )

        }

    }

    fun cargarImagenes(token: String, productoId: Int) {

        viewModelScope.launch {
            productoRepo.getProductImages(token, productoId)
                .onSuccess { imagenesUiState = it }
        }

    }

}

sealed class VerProductoUiState() {

    object Idle: VerProductoUiState()

    object Loading: VerProductoUiState()

    data class SuccessCargarProducto(val resp: Producto): VerProductoUiState()

    val imagenes: List<ImagenDetalle> = emptyList()

    data class Error(val message: String): VerProductoUiState()

}