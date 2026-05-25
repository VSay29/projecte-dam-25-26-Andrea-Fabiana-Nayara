package com.example.android_loop.view.vistas

import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.example.android_loop.view.componentes.Busqueda_Componente
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.android_loop.R
import com.example.android_loop.utils.getToken
import com.example.android_loop.utils.navegacionConfig.ROUTES
import com.example.android_loop.utils.setToken
import com.example.android_loop.utils.sinAcentos
import com.example.android_loop.utils.tokenValido
import com.example.android_loop.view.theme.Android_LoopTheme
import com.example.android_loop.viewModel.FavoritosUiState
import com.example.android_loop.viewModel.FavoritosViewModel

@Composable
fun Favoritos(navController: NavHostController) {

    val context = LocalContext.current
    val token = getToken(context)

    LaunchedEffect(Unit) {
        if (!tokenValido(token)) {

            setToken(context, "")

            Toast.makeText(
                context,
                "La sesión ha caducado",
                Toast.LENGTH_SHORT
            ).show()

            navController.navigate(ROUTES.LOGIN) {
                popUpTo(0)
                launchSingleTop = true
            }
        }
    }

    var filtro by rememberSaveable { mutableStateOf("") }

    // DOC: Esta declaración hace que el viewModel de favoritos esté compartido con el home
    val viewModelFavoritos: FavoritosViewModel = viewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity)
    val favState = viewModelFavoritos.favState

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {

        // Llamar al endpoint

        LaunchedEffect(Unit)  {
            viewModelFavoritos.favoritosGet(token!!)
        }

        // UI

        Column(
            Modifier.fillMaxSize().padding(top = 24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Barra de búsqueda con filtros

            Busqueda_Componente(
                value = filtro,
                onValueChange = { filtro = it },
                placeholder = "Buscar producto",
                modifier = Modifier.padding(12.dp)
            )

            when (favState) {

                is FavoritosUiState.SuccessGet -> {
                    var lista = favState.result

                    // Agregar filtros si los hay

                    if (!filtro.isEmpty()) lista = lista.filter {
                        it.nombre.sinAcentos().lowercase().contains(filtro.sinAcentos().lowercase())
                    }

                    if (lista.isEmpty()) {
                        Text("No hay productos en favoritos")
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(lista) { producto ->

                                val bitmap = remember(producto.imagenes) {
                                    producto.imagenes.firstOrNull()?.let {
                                        try {
                                            val bytes = Base64.decode(it, Base64.DEFAULT)
                                            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
                                        } catch (e: Exception) { null }
                                    }
                                }

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            navController.navigate("${ROUTES.DETALLE_PRODUCTO}/${producto.id}")
                                        },
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(115.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (bitmap != null) {
                                            Image(
                                                bitmap = bitmap,
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .width(115.dp)
                                                    .fillMaxHeight()
                                                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                                            )
                                        } else {
                                            Box(
                                                modifier = Modifier
                                                    .width(115.dp)
                                                    .fillMaxHeight()
                                                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                                                    .background(Color(0xFFE8EEF4)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    "Sin imagen",
                                                    color = Color(0xFF8FA3B1),
                                                    style = MaterialTheme.typography.labelSmall
                                                )
                                            }
                                        }

                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(horizontal = 14.dp, vertical = 10.dp),
                                            verticalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = producto.nombre,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.SemiBold,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis,
                                                color = Color(0xFF1A1A2E)
                                            )

                                            Spacer(Modifier.height(4.dp))

                                            Text(
                                                text = producto.descripcion,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color(0xFF8FA3B1),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )

                                            Spacer(Modifier.height(4.dp))

                                            Text(
                                                text = "%.2f €".format(producto.precio),
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF003459)
                                            )
                                        }

                                        IconButton(
                                            onClick = {
                                                viewModelFavoritos.agregarOquitarfavorito(token!!, producto.id)
                                            },
                                            modifier = Modifier.padding(end = 8.dp).size(40.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.DeleteOutline,
                                                contentDescription = "Eliminar de favoritos",
                                                tint = Color(0xFFE63946),
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                is FavoritosUiState.ErrorGet -> Text(favState.message)

                else -> {}
            }

        }

        // UI de carga

        if (favState is FavoritosUiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.Blue
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun FavoritosPreview() {
    Android_LoopTheme {
        Favoritos(navController = rememberNavController())
    }
}
