package com.example.android_loop.view.vistas
/*
import android.content.Context.MODE_PRIVATE
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.android_loop.R
import com.example.android_loop.view.shoppingCart.CartViewModel
import com.example.android_loop.view.theme.Android_LoopTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Compra(navHostController: NavHostController, viewModel: CartViewModel = viewModel()) {

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)
    val storedToken = prefs.getString("token", null)

    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val metodoSeleccionado = remember { mutableStateOf<Int?>(null) }
    val metodosPago = listOf(
        R.drawable.visa,
        R.drawable.bizum,
        R.drawable.mastercard,
        R.drawable.paypal
    )

    Box(modifier = Modifier.fillMaxSize().background(Color.Transparent)) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 80.dp)
        ) {
            Text(
                text = "Proceso de pago",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Seleccione el método de pago",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(metodosPago) { metodo ->
                    Image(
                        painter = painterResource(id = metodo),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clickable {
                                metodoSeleccionado.value = metodo
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Total: %.2f €".format(viewModel.total))

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Button(onClick = {
                    isLoading = true
                    scope.launch {
                        delay(1000)
                        isLoading = false
                        Toast.makeText(context, "¡Pago realizado!", Toast.LENGTH_SHORT).show()
                    }
                }, enabled = metodoSeleccionado.value != null) {
                    if (metodoSeleccionado.value != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Pagar con el método seleccionado: ")
                            Image(
                                painter = painterResource(id = metodoSeleccionado.value!!),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        Text("Pagar con el método seleccionado")
                    }
                }

            }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun CompraPreview() {
    Android_LoopTheme {
        Compra(navHostController = rememberNavController())
    }
}*/