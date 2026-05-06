package com.example.android_loop.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_loop.data.model_dataClass.favoritoResult.Favorito
import com.example.android_loop.data.repository.FavoritoRepository
import com.example.android_loop.view.vistas.Favoritos
import kotlinx.coroutines.launch

class FavoritosViewModel(private val repo: FavoritoRepository = FavoritoRepository()) : ViewModel() {

    var favState by mutableStateOf<FavoritosUiState>(FavoritosUiState.Idle)
    var favoritoIds by mutableStateOf(setOf<Int>())
        private set

    fun favoritosGet(token : String) {
        viewModelScope.launch {
            favState = FavoritosUiState.Loading

            val result = repo.getUserFavoritos(token)

            favState = result.fold(
                onSuccess = {
                    favoritoIds = it.result.map { fav -> fav.id }.toSet()
                    FavoritosUiState.SuccessGet(it.result)
                },
                onFailure = { FavoritosUiState.ErrorGet(it.message ?: "No se pudo agregar el producto a favoritos") }
            )
        }
    }

    fun agregarOquitarfavorito(token: String, productId: Int) {
        viewModelScope.launch {

            val esFavorito = favoritoIds.contains(productId)

            if(esFavorito) {
                repo.removeFavorito(token, productId)
                favoritoIds = favoritoIds - productId
            } else {
                repo.addFavoritos(token, productId)
                favoritoIds = favoritoIds + productId
            }

            if (favState is FavoritosUiState.SuccessGet) {
                val lista = (favState as FavoritosUiState.SuccessGet).result
                val nuevaLista = if (esFavorito) {
                    lista.filter { it.id != productId }
                } else {
                    favoritosGet(token)
                    return@launch
                }

                favState = FavoritosUiState.SuccessGet(nuevaLista)

            }

        }
    }
}

sealed class FavoritosUiState {

    object Idle : FavoritosUiState()

    object Loading : FavoritosUiState()

    data class SuccessGet(val result: List<Favorito>) : FavoritosUiState()

    data class ErrorGet(val message: String) : FavoritosUiState()

    data class SuccessAdd(val result: Boolean) : FavoritosUiState()

    data class ErrorAdd(val message: String) : FavoritosUiState()

}

