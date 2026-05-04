package com.example.android_loop.viewModel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import com.example.android_loop.data.model_dataClass.productoResult.Producto
import com.example.android_loop.utils.getUserIdFromToken
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val cartJson = Json { ignoreUnknownKeys = true }
private const val PREFS_NAME = "loop_prefs"

class CarritoViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private var cartKey: String = ""

    val cartItems = mutableStateListOf<Producto>()
    val selectedItems = mutableStateListOf<Producto>()

    val total: Double
        get() = selectedItems.sumOf { it.precio }

    init {
        loadCartFromPrefs()
    }

    private fun computeCartKey(): String {
        val token = prefs.getString("token", null) ?: ""
        val userId = getUserIdFromToken(token)?.toString() ?: "guest"
        return "carrito_items_$userId"
    }

    private fun loadCartFromPrefs() {
        cartKey = computeCartKey()
        cartItems.clear()
        selectedItems.clear()
        val saved = prefs.getString(cartKey, null)
        if (saved != null) {
            try {
                cartItems.addAll(cartJson.decodeFromString<List<Producto>>(saved))
            } catch (_: Exception) {}
        }
    }

    fun reloadCart() {
        val newKey = computeCartKey()
        if (newKey != cartKey) loadCartFromPrefs()
    }

    private fun saveCart() {
        prefs.edit().putString(cartKey, cartJson.encodeToString(cartItems.toList())).apply()
    }

    fun addToCart(product: Producto) {
        if (cartItems.none { it.id == product.id }) {
            cartItems.add(product)
            saveCart()
        }
    }

    fun removeFromCart(product: Producto) {
        cartItems.removeAll { it.id == product.id }
        selectedItems.removeAll { it.id == product.id }
        saveCart()
    }

    fun toggleSeleccion(product: Producto, selected: Boolean) {
        if (selected) {
            if (selectedItems.none { it.id == product.id }) selectedItems.add(product)
        } else selectedItems.removeAll { it.id == product.id }
    }
}