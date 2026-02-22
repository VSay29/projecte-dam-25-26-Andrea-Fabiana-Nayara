package com.example.android_loop.Producto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class ProductViewModel : ViewModel() {

    var products by mutableStateOf<List<Product>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadProducts() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = RetrofitClient.api.getProducts("Bearer TU_TOKEN")

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
}
