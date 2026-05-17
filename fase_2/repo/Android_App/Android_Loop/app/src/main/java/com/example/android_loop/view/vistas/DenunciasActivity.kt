package com.example.android_loop.view.vistas

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.android_loop.R
import com.example.android_loop.data.model_dataClass.denunciaResult.Denuncia
import com.example.android_loop.utils.getToken
import com.example.android_loop.utils.navegacionConfig.ROUTES
import com.example.android_loop.utils.setToken
import com.example.android_loop.utils.tokenValido
import com.example.android_loop.viewModel.DenunciaUiState
import com.tuapp.ui.theme.OnPrimary
import com.tuapp.ui.theme.Primary
import com.tuapp.ui.theme.Secondary
import com.example.android_loop.viewModel.DenunciaViewModel

@Composable
fun Denuncias(navController: NavHostController) {

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

    val denunciaViewModel: DenunciaViewModel = viewModel()
    val denunciaState = denunciaViewModel.obtenerDenunciaState
    val retirarState = denunciaViewModel.retirarDenunciaState

    var denunciaARetirar by remember { mutableStateOf<Denuncia?>(null) }

    LaunchedEffect(Unit) {
        denunciaViewModel.obtenerDenuncias(token)
    }

    LaunchedEffect(retirarState) {
        when (retirarState) {
            is DenunciaUiState.SuccessRetirarDenuncia -> {
                Toast.makeText(context, "Denuncia retirada correctamente", Toast.LENGTH_SHORT).show()
                denunciaViewModel.obtenerDenuncias(token)
            }
            is DenunciaUiState.Error ->
                Toast.makeText(context, "Error al retirar la denuncia", Toast.LENGTH_SHORT).show()
            else -> {}
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Primary, Secondary)
                        )
                    )
                    .padding(top = 48.dp, bottom = 16.dp, end = 20.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = OnPrimary
                    )
                }
                Text(
                    text = "Mis denuncias",
                    style = MaterialTheme.typography.titleLarge,
                    color = OnPrimary
                )
            }

            when (denunciaState) {
                is DenunciaUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF003459))
                    }
                }
                is DenunciaUiState.SuccessObtenerDenuncias -> {
                    val lista = denunciaState.denuncias
                    if (lista.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No has realizado ninguna denuncia", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(lista) { denuncia ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(end = 40.dp)) {
                                            Text(
                                                text = if (denuncia.producto_id != null) "Denuncia de producto" else "Denuncia de reseña",
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 14.sp,
                                                color = Color(0xFF003459)
                                            )
                                            Spacer(Modifier.height(6.dp))
                                            Text(
                                                text = "Motivo: ${denuncia.motivo_denuncia}",
                                                fontSize = 12.sp,
                                                color = Color.Gray
                                            )
                                            denuncia.producto_nombre?.let {
                                                Spacer(Modifier.height(4.dp))
                                                Text(
                                                    text = "Producto denunciado: $it",
                                                    fontSize = 12.sp,
                                                    color = Color.Gray
                                                )
                                            }
                                            denuncia.comentario_texto?.let {
                                                Spacer(Modifier.height(4.dp))
                                                Text(
                                                    text = "Reseña denunciada: \"$it\"",
                                                    fontSize = 12.sp,
                                                    color = Color.Gray
                                                )
                                            }
                                            denuncia.usuario_denunciado_nombre?.let {
                                                Spacer(Modifier.height(4.dp))
                                                Text(
                                                    text = "Usuario: $it",
                                                    fontSize = 12.sp,
                                                    color = Color.Gray
                                                )
                                            }
                                        }
                                        Icon(
                                            painter = painterResource(R.drawable.trash),
                                            contentDescription = "Retirar denuncia",
                                            modifier = Modifier
                                                .size(28.dp)
                                                .align(Alignment.TopEnd)
                                                .clickable { denunciaARetirar = denuncia },
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                is DenunciaUiState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(denunciaState.message, color = Color.Red)
                    }
                }
                else -> {}
            }
        }
    }

    denunciaARetirar?.let { denuncia ->
        AlertDialog(
            onDismissRequest = { denunciaARetirar = null },
            title = { Text("Retirar denuncia") },
            text = { Text("¿Estás seguro de que quieres retirar esta denuncia? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        denunciaViewModel.retirarDenuncia(token, denuncia.id)
                        denunciaARetirar = null
                    }
                ) { Text("Retirar", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { denunciaARetirar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
