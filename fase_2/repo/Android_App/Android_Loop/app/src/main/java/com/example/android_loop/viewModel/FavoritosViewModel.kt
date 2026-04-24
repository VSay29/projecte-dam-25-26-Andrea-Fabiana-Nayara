package com.example.android_loop.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_loop.data.model_dataClass.favoritoResult.Favorito
import com.example.android_loop.data.repository.FavoritoRepository
import kotlinx.coroutines.launch

class FavoritosViewModel(private val repo: FavoritoRepository = FavoritoRepository()) : ViewModel() {

    var favState by mutableStateOf<FavoritosUiState>(FavoritosUiState.Idle)

    fun favoritosGet(token : String) {
        viewModelScope.launch {
            favState = FavoritosUiState.Loading

            val result = repo.getUserFavoritos(token)

            favState = result.fold(
                onSuccess = { FavoritosUiState.SuccessGet(it.result) },
                onFailure = { FavoritosUiState.ErrorGet(it.message ?: "Hubo un error") }
            )
        }
    }

    fun favoritosDelete(token: String, productoId: Int) {

        viewModelScope.launch {

            repo.removeFavorito(token, productoId)

            if (favState is FavoritosUiState.SuccessGet) {
                val currentLista = (favState as FavoritosUiState.SuccessGet).result

                val nuevaLista = currentLista.filter { it.id != productoId }

                favState = FavoritosUiState.SuccessGet(nuevaLista)

            }

        }
    }

    fun favoritosAdd(token: String, productoId: Int) {
        viewModelScope.launch {
            val result = repo.addFavoritos(token, productoId)

            favState = result.fold(
                onSuccess = { FavoritosUiState.SuccessAdd(it.success) },
                onFailure = { FavoritosUiState.ErrorAdd(it.message ?: "Hubo un error") }
            )
        }
    }

    /* TODO: ORIENTACIÓN PARA QUITAR FAVORITOS Y AGREGARLOS CONTANDO CON EL ICONO DEL HOME
    //estado que guara los ids de productos favoritos
    var favoritoIds by mutableStateOf(setOf<Int>())
        private set


    fun añadirOquitarfavorito(token: String, productId: Int) {
        viewModelScope.launch {
            favoritoIds = if (favoritoIds.contains(productId)) {
                repository.removeFavorito(token, productId)
                favoritoIds - productId   // Ya era favorito → lo quitamos
            } else {
                repository.addFavoritos(token, productId)
                favoritoIds + productId   // No era favorito → lo añadimos
            }


        }
    }
}*/

}

sealed class FavoritosUiState {

    object Idle : FavoritosUiState()

    object Loading : FavoritosUiState()

    data class SuccessGet(val result: List<Favorito>) : FavoritosUiState()

    data class ErrorGet(val message: String) : FavoritosUiState()

    data class SuccessAdd(val result: Boolean) : FavoritosUiState()

    data class ErrorAdd(val message: String) : FavoritosUiState()

}

