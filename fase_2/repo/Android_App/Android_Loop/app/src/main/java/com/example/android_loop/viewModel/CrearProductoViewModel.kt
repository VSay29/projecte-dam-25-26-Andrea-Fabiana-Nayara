package com.example.android_loop.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_loop.data.model_dataClass.categoriaResult.Categoria
import com.example.android_loop.data.model_dataClass.etiquetaResult.CreateEtiquetaRequest
import com.example.android_loop.data.model_dataClass.etiquetaResult.Etiqueta
import com.example.android_loop.data.model_dataClass.productoResult.CreateProductRequest
import com.example.android_loop.data.model_dataClass.productoResult.ImageRequest
import com.example.android_loop.data.repository.EtiquetaRepository
import com.example.android_loop.data.repository.ProductoRepository
import com.example.android_loop.utils.toBase64
import kotlinx.coroutines.launch

class CrearProductoViewModel(private val productoRepo: ProductoRepository = ProductoRepository(), private val etiquetaRepo: EtiquetaRepository = EtiquetaRepository()): ViewModel() {

    var crearProductoUiState by mutableStateOf<CrearProductoUiState>(CrearProductoUiState.Idle)
    var crearEtiquetaUiState by mutableStateOf<CrearProductoUiState>(CrearProductoUiState.Idle)
    var cargarEtiquetaUiState by mutableStateOf<CrearProductoUiState>(CrearProductoUiState.Idle)
    var obtenerEtiquetaPorIdUiState by mutableStateOf<CrearProductoUiState>(CrearProductoUiState.Idle)

    var cargarCategoriasUiState by mutableStateOf<CrearProductoUiState>(CrearProductoUiState.Idle)


    fun crearProducto(
        token: String,
        context: Context,
        nombre: String,
        descripcion: String,
        precio: Double,
        estado: String,
        ubicacion: String,
        antiguedad: String,
        categoriaId: Int,
        etiquetaIds: List<Int>,
        imageUris: List<Uri>) {

        viewModelScope.launch {

            crearProductoUiState = CrearProductoUiState.Loading
            Log.d("DEBUG_CREARPRODUCTO", "CREANDO PRODUCTO")
            val listaImagenes = convertirListImgToListB64(imageUris, context)

            val result = productoRepo.createProduct(token, CreateProductRequest(nombre, descripcion, precio, estado, ubicacion, antiguedad, categoriaId, etiquetaIds, listaImagenes))

            crearProductoUiState = result.fold(
                onSuccess = {
                    Log.d("DEBUG_CREARPRODUCTO", "PRODUCTO OK")
                    CrearProductoUiState.SuccessCrearProducto(it.ok) },
                onFailure = { CrearProductoUiState.Error(it.message ?: "No se pudo crear el producto") }
            )

            Log.d("DEBUG_CREARPRODUCTO", "$result")

        }

    }

    fun crearEtiqueta(token: String, name: String, onCreated: (Int) -> Unit) {

        viewModelScope.launch {

            crearEtiquetaUiState = CrearProductoUiState.Loading
            Log.d("DEBUG_CREARETIQUETA", "CREANDO ETIQUETA")

            val result = etiquetaRepo.createEtiqueta(token, CreateEtiquetaRequest(name, true))

            crearEtiquetaUiState = result.fold(
                onSuccess = {
                    onCreated(it.etiqueta_id)
                    cargarEtiquetas(token)
                    CrearProductoUiState.Idle
                },
                onFailure = { CrearProductoUiState.Error(it.message ?: "No se ha podido crear la etiqueta") }
            )

            Log.d("RESULT_CREARETIQUETA", "$result")

        }

    }

    fun cargarEtiquetas(token: String) {

        viewModelScope.launch {

            cargarEtiquetaUiState = CrearProductoUiState.Loading

            val result = etiquetaRepo.getEtiquetas(token)

            cargarEtiquetaUiState = result.fold(
                onSuccess = { CrearProductoUiState.SuccessCargarEtiquetas(it) },
                onFailure = { CrearProductoUiState.Error(it.message ?: "No fue posible obtener las etiquetas") }
            )

        }

    }

    fun obtenerEtiquetaPorId(token: String, id: Int) {

        viewModelScope.launch {

            obtenerEtiquetaPorIdUiState = CrearProductoUiState.Loading

            val result = etiquetaRepo.getEtiquetas(token)
            obtenerEtiquetaPorIdUiState = result.fold(
                onSuccess = { it -> CrearProductoUiState.SuccessObtenerEtiquetaPorId(it.filter { it.id == id }[0]) },
                onFailure = { CrearProductoUiState.Error(it.message ?: "No fue posible obtener las etiquetas") }
            )

        }

    }

    fun cargarCategorias(token: String) {
        viewModelScope.launch {

            cargarCategoriasUiState = CrearProductoUiState.Loading
            val result = productoRepo.getCategoriasProductos(token)

            cargarCategoriasUiState = result.fold(
                onSuccess = { CrearProductoUiState.SuccessCargarCategorias(it) },
                onFailure = { CrearProductoUiState.Error(it.message ?: "No fue posible cargar las categorías") }
            )
        }
    }

}

sealed class CrearProductoUiState {

    object Idle: CrearProductoUiState()

    object Loading: CrearProductoUiState()

    data class SuccessCrearProducto(val resp: Boolean): CrearProductoUiState()

    data class SuccessCrearEtiqueta(val resp: Int): CrearProductoUiState()

    data class SuccessCargarEtiquetas(val resp: List<Etiqueta>): CrearProductoUiState()

    data class SuccessObtenerEtiquetaPorId(val resp: Etiqueta): CrearProductoUiState()

    data class SuccessCargarCategorias(val resp: List<Categoria>): CrearProductoUiState()

    data class Error(val message: String): CrearProductoUiState()

}

private fun convertirListImgToListB64(imageUris: List<Uri>, context: Context): List<ImageRequest> {

    val imageRequest = mutableListOf<ImageRequest>()

    imageUris.forEachIndexed { index, uri ->

        val base64Image = toBase64(context, uri)

        imageRequest.add(
            ImageRequest(
                imagen = base64Image,
                is_principal = index == 0,
                sequence = index + 1
            )
        )

    }

    return imageRequest

}