package com.example.android_loop.Producto

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.android_loop.ui.shoppingCart.CartViewModel


@Composable
fun ProductScreen(
    viewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    navController: NavController
) {

    val products = viewModel.products
    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Button(
            onClick = { navController.navigate("crear_producto") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear Producto")
        }

        Button(
            onClick = { viewModel.loadProducts() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Listar productos")
        }

        Button(
            onClick = { navController.navigate("carrito") },
            modifier = Modifier.fillMaxWidth()
        ) {
            val count = cartViewModel.cartItems.size
            Text(if (count > 0) "Ver carrito ($count)" else "Ver carrito")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                CircularProgressIndicator()
            }

            error != null -> {
                Text(
                    text = "Error: $error",
                    color = MaterialTheme.colorScheme.error
                )
            }

            else -> {
                LazyColumn {
                    items(products) { product ->
                        ProductItem(
                            product = product,
                            onAddToCart = { cartViewModel.addToCart(product) },
                            isInCart = cartViewModel.cartItems.any { it.id == product.id }
                        )
                    }
                }
            }
        }
    }
}
