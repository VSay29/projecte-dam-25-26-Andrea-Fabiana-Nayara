package com.example.android_loop.Producto

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.*


import android.util.Base64

class ProductViewModel : ViewModel() {

    var products by mutableStateOf<List<Product>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)

    //estado para que muestre mensaje cuando se crea un producto
    var productCreated by mutableStateOf(false)
        private set

    fun resetProductCreated() {
        productCreated = false
    }


    fun loadProducts() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = RetrofitClient.api.getProducts()

                if (response.isSuccessful) {
                    products = response.body()?.products ?: emptyList()
                } else {
                    errorMessage = "Error ${response.code()}"
                }

            } catch (e: Exception) {
                errorMessage = e.message
            }

            isLoading = false
        }
    }

    private fun uriToBase64(context: Context, uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    fun createProduct(
        context: Context,
        nombre: String,
        descripcion: String,
        precio: Double,
        estado: String,
        ubicacion: String,
        antiguedad: String,
        categoriaId: Int,
        imageUris: List<Uri>
    ) {

        viewModelScope.launch {

            isLoading = true
            errorMessage = null

            try {

                if (imageUris.isEmpty()) {
                    errorMessage = "Debes seleccionar al menos una imagen"
                    isLoading = false
                    return@launch
                }



                // 🔥 Convertir TODAS las imágenes a Base64
                val imagenesList = imageUris.mapIndexed { index, uri ->

                    val base64Image = uriToBase64(context, uri)

                    ImageRequest(
                        imagen = base64Image,
                        is_principal = index == 0,   // primera es principal
                        sequence = index + 1
                    )
                }

                // 🔥 Construir request JSON
                val request = CreateProductRequest(
                    nombre = nombre,
                    descripcion = descripcion,
                    precio = precio,
                    estado = estado,
                    ubicacion = ubicacion,
                    antiguedad = antiguedad,
                    categoria_id = categoriaId,
                    imagenes = imagenesList
                )

                // 🔥 Llamada al API

                val jsonRpcBody = JsonRpcRequest(
                    params = request
                )

                val response = RetrofitClient.api.createProduct(jsonRpcBody)

                println("CODE: ${response.code()}")
                println("BODY: ${response.body()}")
                println("ERROR BODY: ${response.errorBody()?.string()}")

                if (response.isSuccessful) {
                    val result = response.body()?.result

                    if (result?.ok == true) {
                        productCreated = true
                    }

                } else {
                    errorMessage = "Error ${response.code()}"
                }

            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            }

            isLoading = false
        }
    }


    fun createEtiqueta(name: String) {

        viewModelScope.launch {

            try {

                val request = CreateEtiquetaRequest(
                    data = EtiquetaData(name = name)
                )

                val response = RetrofitClient.api.createEtiqueta(request)

                if (response.isSuccessful) {

                    val result = response.body()

                    if (result?.success == true) {
                        println("Etiqueta creada ID: ${result.etiqueta_id}")
                    } else {
                        errorMessage = result?.error ?: "Error desconocido"
                    }

                } else {
                    errorMessage = "Error ${response.code()}"
                }

            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    var etiquetas by mutableStateOf<List<Etiqueta>>(emptyList())
        private set

    fun loadEtiquetas() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getEtiquetas()
                if (response.isSuccessful) {
                    etiquetas = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

}
