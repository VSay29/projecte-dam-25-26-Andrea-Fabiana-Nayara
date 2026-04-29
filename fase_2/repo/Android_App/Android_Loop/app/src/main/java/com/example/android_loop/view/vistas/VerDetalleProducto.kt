package com.example.android_loop.view.vistas

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.filled.Share
import com.example.android_loop.view.componentes.BotonCrearProducto
import com.tuapp.ui.theme.Primary
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.wear.compose.material3.IconButton
import com.example.android_loop.data.model_dataClass.categoriaResult.Categoria
import com.example.android_loop.data.model_dataClass.etiquetaResult.Etiqueta
import com.example.android_loop.data.model_dataClass.productoResult.Imagen
import com.example.android_loop.data.model_dataClass.productoResult.ImagenDetalle
import com.example.android_loop.data.model_dataClass.productoResult.Producto
import com.example.android_loop.data.model_dataClass.productoResult.Propietario
import com.example.android_loop.utils.getToken
import com.example.android_loop.utils.navegacionConfig.ROUTES
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.android_loop.viewModel.CarritoViewModel
import com.example.android_loop.viewModel.VerProductoUiState
import com.example.android_loop.viewModel.VerProductoViewModel
import kotlin.collections.emptyList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerProducto(productoId: Int, navController: NavController) {

    Log.d("DEBUG_IDPRODUCTO", productoId.toString())

    val context = LocalContext.current
    val token = getToken(context)

    // SECCION: Declaración de viewModels

    val verProductoVM: VerProductoViewModel = viewModel()
    val verProductoState = verProductoVM.verProductoUiState
    val carritoViewModel: CarritoViewModel = viewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity)

    // SECCION: VARIABLES

    var id by rememberSaveable { mutableIntStateOf(0) }
    var nombre by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var precio by rememberSaveable { mutableDoubleStateOf(0.0) }
    var estado by rememberSaveable { mutableStateOf("nuevo") }
    var ubicacion by rememberSaveable { mutableStateOf("") }
    var antiguedad: String? by rememberSaveable { mutableStateOf("") }
    var categoria by remember { mutableStateOf<Categoria?>(null) }
    var propietario by remember { mutableStateOf<Propietario?>(null) }
    var etiquetas by rememberSaveable { mutableStateOf(emptyList<Etiqueta>()) }
    var thumbnail: String? by rememberSaveable { mutableStateOf("") }

    var showImageViewer by remember { mutableStateOf(false) }

    // SECCION: CARGAR PRODUCTO

    LaunchedEffect(productoId) {
        verProductoVM.cargarProducto(token, productoId)
        verProductoVM.cargarImagenes(token, productoId)
    }

    // FIXME: SUSTITUIR LAS VARIABLES POR LA VARIABLE PRODUCTO (NOS AHORRAMOS 10 VARIBLES)
    val producto = (verProductoState as? VerProductoUiState.SuccessCargarProducto)?.resp

    val listaImagenes = verProductoVM.imagenesUiState

    LaunchedEffect(verProductoState) {

        when (val producto = verProductoState) {

            is VerProductoUiState.SuccessCargarProducto -> {
                id = producto.resp.id
                nombre = producto.resp.nombre
                descripcion = producto.resp.descripcion
                precio = producto.resp.precio
                estado = producto.resp.estado
                ubicacion = producto.resp.ubicacion
                antiguedad = producto.resp.antiguedad
                categoria = producto.resp.categoria
                propietario = producto.resp.propietario
                etiquetas = producto.resp.etiquetas
                thumbnail = producto.resp.thumbnail
            }

            is VerProductoUiState.Error -> {
                // URGENTE: MOSTRAR DIALOGO INFORMATIVO DE QUE NO ESTÁ DISPONIBLE EL PRODUCTO
                Toast.makeText(context, producto.message, Toast.LENGTH_SHORT).show()
            }

            else -> {}

        }
    }

    Scaffold(
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            navController.navigate("perfil_Vendedor/${propietario?.id}/${Uri.encode(propietario?.nombre)}")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(50.dp),
                        border = BorderStroke(1.5.dp, Primary),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Primary,
                            containerColor = Color.White
                        )
                    ) {
                        Text("Ver perfil", style = MaterialTheme.typography.labelLarge)
                    }
                    BotonCrearProducto(
                        texto = "Añadir al carrito",
                        onClick = {
                            val imagenesParaProducto = listaImagenes.map {
                                Imagen(id = it.id, principal = it.principal, orden = it.sequence)
                            }
                            carritoViewModel.addToCart(
                                Producto(id, nombre, descripcion, precio, estado, ubicacion, antiguedad, categoria, propietario, etiquetas, imagenesParaProducto, thumbnail)
                            )
                            navController.navigate(ROUTES.CARRITO)
                        },
                        modifier = Modifier.weight(1f),
                        enabled = true
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {

            Box(modifier = Modifier.fillMaxWidth()) {
                ImageCarousel(imagenes = listaImagenes, onImageClick = { showImageViewer = true })

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart)
                        .statusBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        Icons.Default.ArrowBackIosNew,
                        contentDescription = "Volver",
                        tint = Color.White,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { navController.popBackStack() }
                    )

                    Icon(
                        Icons.Default.Share,
                        contentDescription = "Compartir",
                        tint = Color.White,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                val shareText = "$nombre\n${"%.2f €".format(precio)}\n\n$descripcion"
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                }
                                context.startActivity(Intent.createChooser(intent, "Compartir producto"))
                            }
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-20).dp),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = MaterialTheme.colorScheme.background,
                shadowElevation = 0.dp
            ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)) {

                Text(
                    text = nombre.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "%.2f €".format(precio),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFE8E8E8)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            text = "Descripción",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = descripcion,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = Color(0xFFE8E8E8))
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Características",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        InfoRow("Estado", estado)
                        InfoRow("Ubicación", ubicacion)
                        antiguedad?.let {
                            InfoRow("Antigüedad", it)
                        }
                        InfoRow("Vendedor", propietario?.nombre)

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = Color(0xFFE8E8E8))
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Categoría",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            )
                            Text(
                                text = categoria?.nombre ?: "Sin categoría",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        if (etiquetas.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = Color(0xFFE8E8E8))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Etiquetas",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                etiquetas.forEach { etiqueta ->
                                    SuggestionChip(
                                        onClick = {},
                                        label = { Text(etiqueta.displayName) }
                                    )
                                }
                            }
                        }

                    }
                }

            }
            } // Surface

        }

        if (showImageViewer) {
            Dialog(
                onDismissRequest = { showImageViewer = false },
                properties = androidx.compose.ui.window.DialogProperties(
                    usePlatformDefaultWidth = false
                )
            ) {
                Box(Modifier.fillMaxSize().background(Color.Black)) {
                    MostrarImagenes(listaImagenes)

                    IconButton(
                        onClick = { showImageViewer = false },
                        Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.White
                        )
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImageCarousel(imagenes: List<ImagenDetalle>, onImageClick: () -> Unit) {

    if (imagenes.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .background(Color(0xFFE0E0E0)),
            contentAlignment = Alignment.Center
        ) {
            Text("Sin imágenes")
        }
        return
    }

    val ordenadas = remember(imagenes) {
        imagenes.sortedWith(
            compareByDescending<ImagenDetalle> { it.principal }.thenBy { it.sequence }
        )
    }

    val pagerState = rememberPagerState(pageCount = { ordenadas.size })

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val img = ordenadas[page]
            val bitmap = remember(img.id) {
                img.imagen.let {
                    try {
                        val bytes = Base64.decode(it, Base64.DEFAULT)
                        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
                    } catch (e: Exception) {
                        null
                    }
                }
            }

            if (bitmap != null) {
                Image(
                    bitmap = bitmap,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().clickable { onImageClick() }
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Sin imagen", color = Color.Gray)
                }
            }
        }

        // Indicadores de página (puntos)
        if (ordenadas.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(ordenadas.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == pagerState.currentPage) 10.dp else 7.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == pagerState.currentPage) Color.White
                                else Color.White.copy(alpha = 0.5f)
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        if (value != null) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun MostrarImagenes(imagenes: List<ImagenDetalle>) {

    val ordenadas = remember(imagenes) {
        imagenes.sortedWith(compareByDescending<ImagenDetalle> { it.principal }.thenBy { it.sequence })
    }

    val pagerState = rememberPagerState(pageCount = { ordenadas.size })

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        val img = ordenadas[page]

        val bitmap: ImageBitmap? = remember(img.id) {
            try {
                val bytes = Base64.decode(img.imagen, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    ?.asImageBitmap()
            } catch (e: Exception) {
                null
            }
        }

        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

}
