package com.example.android_loop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.android_loop.Producto.CreateProductScreen
import com.example.android_loop.Producto.ProductScreen
import com.example.android_loop.Producto.ProductViewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ProductViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "pantalla_listado"
            ) {

                composable("crear_producto") {
                    CreateProductScreen(viewModel, navController)
                }

                composable("pantalla_listado") {
                    ProductScreen(viewModel, navController)
                }
            }
        }
    }
}