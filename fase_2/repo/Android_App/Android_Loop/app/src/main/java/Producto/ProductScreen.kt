package com.example.android_loop.Producto

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProductScreen(
    viewModel: ProductViewModel = viewModel()
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
            onClick = { viewModel.loadProducts() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Listar productos")
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
                        ProductItem(product)
                    }
                }
            }
        }
    }
}
