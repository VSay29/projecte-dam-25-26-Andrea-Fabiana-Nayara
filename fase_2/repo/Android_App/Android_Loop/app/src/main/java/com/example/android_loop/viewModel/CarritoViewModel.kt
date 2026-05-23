package com.example.android_loop.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_loop.data.model_dataClass.carritoResult.ProductoCarrito
import com.example.android_loop.data.model_dataClass.productoResult.Producto
import com.example.android_loop.data.repository.CarritoApiRepository
import kotlinx.coroutines.launch

class CarritoViewModel(private val repo: CarritoApiRepository = CarritoApiRepository()) : ViewModel() {

    val cartItems = mutableStateListOf<ProductoCarrito>()
    val selectedItems = mutableStateListOf<ProductoCarrito>()

    val total: Double
        get() = selectedItems.sumOf { it.precio }

    fun cargarCarrito(token: String) {
        viewModelScope.launch {
            repo.getCarrito(token).onSuccess { result ->
                cartItems.clear()
                selectedItems.clear()
                cartItems.addAll(result.productos)
            }
        }
    }

    fun addToCart(token: String, product: Producto) {
        if (cartItems.any { it.id == product.id }) return
        viewModelScope.launch {
            repo.addCarrito(token, product.id).onSuccess {
                cartItems.add(
                    ProductoCarrito(
                        id = product.id,
                        nombre = product.nombre,
                        descripcion = product.descripcion,
                        precio = product.precio,
                        ubicacion = product.ubicacion,
                        imagenes = emptyList(),
                        propietario = product.propietario!!
                    )
                )
            }
        }
    }

    fun removeFromCart(token: String, product: ProductoCarrito) {
        viewModelScope.launch {
            repo.removeCarrito(token, product.id).onSuccess {
                cartItems.removeAll { it.id == product.id }
                selectedItems.removeAll { it.id == product.id }
            }
        }
    }

    fun toggleSeleccion(product: ProductoCarrito, selected: Boolean) {
        if (selected) {
            if (selectedItems.none { it.id == product.id }) selectedItems.add(product)
        } else selectedItems.removeAll { it.id == product.id }
    }
}
