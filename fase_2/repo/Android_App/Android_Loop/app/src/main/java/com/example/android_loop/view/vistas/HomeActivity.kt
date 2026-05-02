package com.example.android_loop.view.vistas

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.android_loop.R
import com.example.android_loop.data.Producto.ProductCardSquare
import com.example.android_loop.data.model_dataClass.productoResult.Producto
import com.example.android_loop.utils.getToken
import com.example.android_loop.utils.navegacionConfig.ROUTES
import com.example.android_loop.utils.sinAcentos
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import com.example.android_loop.viewModel.CarritoViewModel
import com.example.android_loop.viewModel.HomeUiState
import com.example.android_loop.viewModel.HomeViewModel
import com.example.android_loop.viewModel.ProductoHomeUiState
import com.tuapp.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavHostController) {

    // SECCION: TOKEN

    val context = LocalContext.current
    val token = getToken(context)

    // SECCION: CARGA DE VIEWMODELS

    val homeViewModel: HomeViewModel = viewModel()
    val homeUiState = homeViewModel.homeUiState
    val productoHomeState = homeViewModel.productosUiState

    // DOC: Este view model es de la vista carrito pero también lo usaremos aquí exclusivamente
    // DOC: para el contador de productos en el carrito ya que la información es compartida

    val carritoViewModel: CarritoViewModel = viewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity)

    // SECCION: VARIABLES

    val productos = (productoHomeState as? ProductoHomeUiState.SuccessCargarProductos)?.resp ?: emptyList()

    val cartCount = carritoViewModel.cartItems.size

    var buscador by remember { mutableStateOf("") }

    var categoriaSeleccionada by remember { mutableStateOf<String?>(null) }


    // SECCION: CARGA DE DATOS DE PRODUCTOS Y CATEGORIAS

    // Se recarga el carrito para que se cargue el carrito correspondiente al usuario que inició sesión
    LaunchedEffect(token) {
        carritoViewModel.reloadCart()
        homeViewModel.cargarProductos(token)
        homeViewModel.cargarCategorias(token)
    }

    // SECCION: FILTRADO Y AGRUPACIÓN DE PRODUCTOS Y CATEGORIAS

    val productosFiltrados = remember(productos, buscador) {
        filtrarProductos(productos, buscador)
    }

    val categoriasCargadas = remember(productosFiltrados) {
        obtenerCategorias(productosFiltrados)
    }

    val productosPorCategoria = remember(productosFiltrados, categoriaSeleccionada) {
        agruparPorCategoria(productosFiltrados, categoriaSeleccionada)
    }

    // SECCION: CONTROL DE LOS ESTADOS

    when(homeUiState) {

        is HomeUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }

        is HomeUiState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Log.d("DEBUG_HOME_HOMESTATE", homeUiState.message)
                Text(homeUiState.message)
            }
            return
        }

        else -> {}

    }

    when(productoHomeState) {

        is ProductoHomeUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }

        is ProductoHomeUiState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Log.d("DEBUG_HOME_PRODUCTOHOMESTATE", productoHomeState.message)
                Text(productoHomeState.message)
            }
            return
        }

        else -> {}

    }

    // SECCION: VISTA

    Scaffold(
        topBar = {
            TopAppBar(

                windowInsets = WindowInsets(0),
                title = {
                    Text(
                        "Página principal",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {

                    // DOC: Icono del carrito con badge que muestra la cantidad de artículos
                    BadgedBox(
                        badge = {
                            if (cartCount > 0) {
                                Badge { Text("$cartCount") }
                            }
                        }
                    ) {
                        IconButton(onClick = { navController.navigate(ROUTES.CARRITO) }) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Carrito"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // SECCION: Barra de búsqueda
            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = buscador,
                onValueChange = { buscador = it },
                placeholder = { Text("Buscar productos...") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.lupa),
                        contentDescription = "Buscar",
                        modifier = Modifier.size(20.dp)
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(30.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF0F4F8),
                    unfocusedContainerColor = Color(0xFFF0F4F8),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(12.dp))


            // SECCION: Chips de categorías
            if (categoriasCargadas.isNotEmpty()) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // DOC: Chip "Todos" para quitar el filtro de categoría
                    item {
                        FilterChip(
                            selected = categoriaSeleccionada == null,
                            onClick = { categoriaSeleccionada = null },
                            label = { Text("Todos") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Primary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }

                    items(categoriasCargadas) { categoria ->
                        FilterChip(
                            selected = categoriaSeleccionada == categoria,
                            onClick = {
                                // DOC: Si ya estaba seleccionada, la deseleccionamos
                                categoriaSeleccionada = if (categoriaSeleccionada == categoria) null else categoria
                            },
                            label = { Text(categoria) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Primary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }


            if (productosFiltrados.isEmpty() && buscador.isNotBlank()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No se encontraron productos")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    productosPorCategoria.forEach { (categoria, productosDeCategoria) ->

                        item {
                            Column {
                                Row(
                                    Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = categoria,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text("${productosDeCategoria.size} artículos")
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(productosDeCategoria) { product ->
                                        ProductCardSquare(
                                            product = product,
                                            onClick = { navController.navigate("${ROUTES.DETALLE_PRODUCTO}/${product.id}") },
                                            onAddToCart = { carritoViewModel.addToCart(product) },
                                            isFavorite = false,
                                            onToggleFavorite = {}
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * DOC: FUNCIÓN PARA FILTRAR LOS PRODUCTOS MEDIANTE EL BUSCADOR
 * DOC: DEVUELVE LA LISTA DE PRODUCTOS FILTRADOS
 */

fun filtrarProductos(productos: List<Producto>, buscador: String): List<Producto> {
    if(buscador.isEmpty()) return productos

    return productos.filter {
        textoNormalizado(it.nombre).contains(textoNormalizado(buscador))
    }
}

// DOC: DEVUELVE LAS CATEGORIAS DE LOS PRODUCTOS QUE TENEMOS INDEPENDIENTEMENTE
// DOC: DE SI ESTÁN FILTRADOS O NO

fun obtenerCategorias(productos: List<Producto>): List<String> {
    return productos.mapNotNull { it.categoria?.nombre }.distinct()
}

// DOC: AGRUPA LA LISTA DE PRODUCTOS POR LA CATEGORIA SELECCIONADA SI LA HAY
// DOC: Y DEVUELVE UN MAP CON LA CATEGORIA COMO CLAVE Y LA LISTA DE PRODUCTOS COMO VALOR

fun agruparPorCategoria(productos: List<Producto>, categoriaSeleccionada: String?): Map<String, List<Producto>> {

    val lista = if (categoriaSeleccionada != null) {
        productos.filter { it.categoria?.nombre == categoriaSeleccionada }
    } else productos

    return lista.groupBy { it.categoria?.nombre ?: "Sin categoría" }

}

/**
 * DOC: NORMALIZA EL TEXTO A MINÚSCULAS Y SIN ACENTOS (FUNCIÓN DE UTILS)
 */
fun textoNormalizado(texto: String): String {
    return texto.sinAcentos().lowercase()
}
