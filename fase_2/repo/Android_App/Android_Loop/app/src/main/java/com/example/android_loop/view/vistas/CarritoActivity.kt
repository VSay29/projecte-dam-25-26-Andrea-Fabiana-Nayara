package com.example.android_loop.view.vistas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.android_loop.utils.getToken
import com.example.android_loop.utils.navegacionConfig.ROUTES
import com.example.android_loop.view.componentes.Boton_Componente
import com.example.android_loop.view.componentes.Header_Componente
import com.example.android_loop.viewModel.CarritoViewModel

@Composable
fun Carrito(navController: NavController) {
    val context = LocalContext.current
    val token = getToken(context)

    val viewModel: CarritoViewModel = viewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity)
    val items = viewModel.cartItems
    val total = viewModel.total

    Column(Modifier.fillMaxSize()) {

        Header_Componente(
            titulo = "Carrito",
            onBack = { navController.popBackStack() }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
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
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(items, key = { it.id }) { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = product.nombre,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = product.descripcion,
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                }
                                Text(
                                    text = "%.2f €".format(product.precio),
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF003459),
                                    fontSize = 15.sp
                                )
                                IconButton(onClick = { viewModel.removeFromCart(token, product) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar del carrito",
                                        tint = Color.Gray
                                    )
                                }
                                val checked = viewModel.selectedItems.any { it.id == product.id }
                                Checkbox(
                                    checked = checked,
                                    onCheckedChange = { viewModel.toggleSeleccion(product, it) }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Total: %.2f €".format(total),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF003459)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Boton_Componente(
                    texto = "Continuar al pago",
                    onClick = { navController.navigate(ROUTES.COMPRA) },
                    enabled = viewModel.selectedItems.isNotEmpty()
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seguir comprando")
            }
        }
    }
}
