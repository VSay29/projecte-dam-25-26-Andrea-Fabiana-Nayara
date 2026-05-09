package com.example.android_loop.viewModel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_loop.data.model_dataClass.categoriaResult.Categoria
import com.example.android_loop.data.model_dataClass.productoResult.Producto
import com.example.android_loop.data.repository.ProductoRepository
import com.example.android_loop.data.repository.UsuarioRepository
import com.example.android_loop.utils.encriptarPasswd
import com.example.android_loop.utils.getToken
import com.example.android_loop.utils.setToken
import com.example.android_loop.utils.tokenExpirado
import kotlinx.coroutines.launch

class HomeViewModel(private val productoRepo: ProductoRepository = ProductoRepository(), private val regenerarToken: GetTokenValidoUseCase): ViewModel() {

    var homeUiState by mutableStateOf<HomeUiState>(HomeUiState.Idle)
    var productosUiState by mutableStateOf<ProductoHomeUiState>(ProductoHomeUiState.Idle)

    fun cargarProductos() {

        viewModelScope.launch {

            val token = regenerarToken()

            productosUiState = ProductoHomeUiState.Loading
            val result = productoRepo.getProductos(token)

            productosUiState = result.fold(
                onSuccess = { ProductoHomeUiState.SuccessCargarProductos(it.products) },
                onFailure = { ProductoHomeUiState.Error(it.message ?: "No fue posible cargar los productos") }
            )
        }
    }

    fun cargarCategorias() {
        viewModelScope.launch {

            val token = regenerarToken()

            homeUiState = HomeUiState.Loading
            val result = productoRepo.getCategoriasProductos(token)

            homeUiState = result.fold(
                onSuccess = { HomeUiState.SuccessCargarCategorias(it) },
                onFailure = { HomeUiState.Error(it.message ?: "No fue posible cargar las categorías") }
            )
        }
    }

}

sealed class HomeUiState {

    object Idle : HomeUiState()

    object Loading : HomeUiState()

    data class SuccessCargarCategorias(val resp: List<Categoria>): HomeUiState()

    data class Error(val message: String): HomeUiState()

}

sealed class ProductoHomeUiState {

    object Idle : ProductoHomeUiState()

    object Loading : ProductoHomeUiState()

    data class SuccessCargarProductos(val resp: List<Producto>): ProductoHomeUiState()

    data class Error(val message: String): ProductoHomeUiState()

}