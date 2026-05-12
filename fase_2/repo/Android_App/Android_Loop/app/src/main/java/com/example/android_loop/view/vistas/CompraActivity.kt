package com.example.android_loop.view.vistas

import android.content.Context.MODE_PRIVATE
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.android_loop.R
import com.example.android_loop.utils.navegacionConfig.ROUTES
import com.example.android_loop.view.componentes.Boton_Componente
import com.example.android_loop.view.componentes.Header_Componente
import com.example.android_loop.view.componentes.Loading_Componente
import com.example.android_loop.viewModel.CarritoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Compra(navController: NavController) {

    val viewModel: CarritoViewModel = viewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity)
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)

    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val metodoSeleccionado = remember { mutableStateOf<Int?>(null) }
    val metodosPago = listOf(
        R.drawable.visa,
        R.drawable.bizum,
        R.drawable.mastercard,
        R.drawable.paypal
    )

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {

        Column(modifier = Modifier.fillMaxSize()) {

            Header_Componente(
                titulo = "Proceso de pago",
                onBack = { navController.popBackStack() }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {

                Text(
                    text = "Método de pago",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF003459)
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(metodosPago) { metodo ->
                        val selected = metodoSeleccionado.value == metodo
                        Card(
                            modifier = Modifier
                                .size(90.dp)
                                .border(
                                    width = if (selected) 2.dp else 1.dp,
                                    color = if (selected) Color(0xFF003459) else Color(0xFFDDDDDD),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { metodoSeleccionado.value = metodo },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selected) Color(0xFFE8EEF4) else Color.White
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = if (selected) 4.dp else 1.dp
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = metodo),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(56.dp)
                                        .padding(4.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Resumen del pedido",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total a pagar",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "%.2f €".format(viewModel.total),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color(0xFF003459)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                if (metodoSeleccionado.value != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Pagando con: ",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                        Image(
                            painter = painterResource(id = metodoSeleccionado.value!!),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Boton_Componente(
                    texto = "Confirmar pago",
                    onClick = {
                        isLoading = true
                        scope.launch {
                            delay(1000)
                            isLoading = false
                            Toast.makeText(context, "¡Pago realizado!", Toast.LENGTH_SHORT).show()
                            navController.navigate(ROUTES.HOME) {
                                popUpTo(ROUTES.HOME) { inclusive = true }
                            }
                        }
                    },
                    enabled = metodoSeleccionado.value != null && !isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Loading_Componente(visible = isLoading)
    }
}
