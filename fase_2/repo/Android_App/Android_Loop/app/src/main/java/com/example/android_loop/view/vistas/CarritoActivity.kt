package com.example.android_loop.view.vistas

import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.android_loop.utils.getToken
import com.example.android_loop.utils.navegacionConfig.ROUTES
import com.example.android_loop.utils.setToken
import com.example.android_loop.utils.tokenValido
import com.example.android_loop.view.componentes.Header_Componente
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.remember
import java.util.Hashtable
import com.example.android_loop.viewModel.CarritoViewModel

@Composable
fun Carrito(navController: NavController) {

    val context = LocalContext.current
    val token = getToken(context)

    LaunchedEffect(Unit) {
        if (!tokenValido(token)) {
            setToken(context, "")
            Toast.makeText(context, "La sesión ha caducado", Toast.LENGTH_SHORT).show()
            navController.navigate(ROUTES.LOGIN) {
                popUpTo(0)
                launchSingleTop = true
            }
        }
    }

    val viewModel: CarritoViewModel = viewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity)
    val items = viewModel.cartItems
    val total = viewModel.total

    val productosAComprar = remember { HashMap<Int, Int>() }

    Column(modifier = Modifier.fillMaxSize()) {

        Header_Componente(
            titulo = "Carrito de compra",
            onBack = { navController.popBackStack() }
        )

        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tu carrito está vacío",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items, key = { it.id }) { product ->

                    val bitmap = remember(product.imagenes) {
                        product.imagenes.firstOrNull()?.let {
                            try {
                                val bytes = Base64.decode(it, Base64.DEFAULT)
                                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
                            } catch (e: Exception) { null }
                        }
                    }

                    val checked = viewModel.selectedItems.any { it.id == product.id }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (bitmap != null) {
                                Image(
                                    bitmap = bitmap,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .width(90.dp)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .width(90.dp)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                                        .background(Color(0xFFE8EEF4)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Sin imagen", color = Color(0xFF8FA3B1), fontSize = 10.sp)
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = product.nombre,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 15.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = product.descripcion,
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "%.2f €".format(product.precio),
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF003459),
                                    fontSize = 14.sp
                                )
                            }

                            IconButton(onClick = { viewModel.removeFromCart(token, product) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar del carrito",
                                    tint = Color.Gray
                                )
                            }

                            Checkbox(
                                checked = checked,
                                onCheckedChange = { isChecked ->
                                        viewModel.toggleSeleccion(product, isChecked)
                                        if(isChecked) productosAComprar[product.id] = product.propietario.id
                                        else productosAComprar.remove(product.id)
                                    }
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray
                )
                Text(
                    text = "%.2f €".format(total),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF003459)
                )
            }

            Button(
                onClick = { navController.navigate(ROUTES.COMPRA) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 12.dp),
                enabled = viewModel.selectedItems.isNotEmpty(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Continuar al pago")
            }
        }
    }
}
