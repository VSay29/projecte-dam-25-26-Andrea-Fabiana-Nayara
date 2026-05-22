package com.example.android_loop.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_loop.data.model_dataClass.categoriaResult.Categoria
import com.example.android_loop.data.model_dataClass.etiquetaResult.CreateEtiquetaRequest
import com.example.android_loop.data.model_dataClass.etiquetaResult.Etiqueta
import com.example.android_loop.data.model_dataClass.productoResult.CreateProductRequest
import com.example.android_loop.data.model_dataClass.productoResult.ImagenDetalle
import com.example.android_loop.data.model_dataClass.productoResult.Producto
import com.example.android_loop.data.model_dataClass.productoResult.UpdateProductRequest
import com.example.android_loop.data.repository.EtiquetaRepository
import com.example.android_loop.data.repository.ProductoRepository
import com.example.android_loop.utils.convertirListImgToListB64
import kotlinx.coroutines.launch

class CrearProductoViewModel(private val productoRepo: ProductoRepository = ProductoRepository(), private val etiquetaRepo: EtiquetaRepository = EtiquetaRepository()): ViewModel() {

    var crearProductoUiState by mutableStateOf<CrearProductoUiState>(CrearProductoUiState.Idle)
    var modificarProductoUiState by mutableStateOf<CrearProductoUiState>(CrearProductoUiState.Idle)
    var cargarProductoIDUiState by mutableStateOf<CrearProductoUiState>(CrearProductoUiState.Idle)
    var crearEtiquetaUiState by mutableStateOf<CrearProductoUiState>(CrearProductoUiState.Idle)
    var cargarEtiquetaUiState by mutableStateOf<CrearProductoUiState>(CrearProductoUiState.Idle)
    var obtenerEtiquetaPorIdUiState by mutableStateOf<CrearProductoUiState>(CrearProductoUiState.Idle)

    var cargarCategoriasUiState by mutableStateOf<CrearProductoUiState>(CrearProductoUiState.Idle)

    var imagenesUiState by mutableStateOf<List<ImagenDetalle>>(emptyList())

    fun cargarImagenes(token: String, productoId: Int?) {

        viewModelScope.launch {
            productoRepo.getProductImages(token, productoId!!)
                .onSuccess { imagenesUiState = it }
        }

    }


    fun crearProducto(token: String, context: Context, nombre: String, descripcion: String, precio: Double, estado: String, ubicacion: String, antiguedad: String, categoriaId: Int, etiquetaIds: List<Int>, imageUris: SnapshotStateList<Uri>) {

        viewModelScope.launch {

            crearProductoUiState = CrearProductoUiState.Loading
            val listaImagenes = convertirListImgToListB64(imageUris, context)

            val result = productoRepo.createProduct(token, CreateProductRequest(nombre, descripcion, precio, estado, ubicacion, antiguedad, categoriaId, etiquetaIds.toList(), listaImagenes))

            crearProductoUiState = result.fold(
                onSuccess = {
                    Log.d("DEBUG_CREARPRODUCTO", "PRODUCTO OK")
                    CrearProductoUiState.SuccessCrearProducto(it.ok) },
                onFailure = { CrearProductoUiState.Error(it.message ?: "No se pudo crear el producto") }
            )

            Log.d("DEBUG_CREARPRODUCTO", "$result")

        }

    }

    fun modificarProducto(token: String, context: Context, id: Int, nombre: String?, descripcion: String?, precio: Double?, estado: String?, categoriaId: Int?, etiquetas: List<Int>?, imageUris: SnapshotStateList<Uri>?) {
        viewModelScope.launch {

            modificarProductoUiState = CrearProductoUiState.Loading
            val listaImagenes = convertirListImgToListB64(imageUris, context)

            val result = productoRepo.modificarProducto(token, id, UpdateProductRequest(nombre, descripcion, precio, estado, categoriaId, etiquetas, listaImagenes))

            modificarProductoUiState = result.fold(
                onSuccess = { CrearProductoUiState.SuccessModificarProducto("Producto modificado con éxito") },
                onFailure = { CrearProductoUiState.Error("El producto no se pudo modificar") }
            )

        }
    }

    fun cargarProducto(token: String, productoId: Int) {

        viewModelScope.launch {

            cargarProductoIDUiState = CrearProductoUiState.Loading
            val result = productoRepo.getProductos(token)

            cargarProductoIDUiState = result.fold(
                onSuccess = { it -> CrearProductoUiState.SuccessCargarProductoID(it.products.filter { it.id == productoId }[0]) },
                onFailure = { CrearProductoUiState.Error(it.message ?: "El producto no está disponible en estos momentos") }
            )

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

    data class SuccessModificarProducto(val resp: String): CrearProductoUiState()

    data class SuccessCargarProductoID(val resp: Producto): CrearProductoUiState()

    data class SuccessCrearEtiqueta(val resp: Int): CrearProductoUiState()

    data class SuccessCargarEtiquetas(val resp: List<Etiqueta>): CrearProductoUiState()

    data class SuccessObtenerEtiquetaPorId(val resp: Etiqueta): CrearProductoUiState()

    data class SuccessCargarCategorias(val resp: List<Categoria>): CrearProductoUiState()

    data class Error(val message: String): CrearProductoUiState()

}