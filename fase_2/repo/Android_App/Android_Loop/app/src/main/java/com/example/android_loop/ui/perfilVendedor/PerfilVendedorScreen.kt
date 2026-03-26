package com.example.android_loop.ui.perfilVendedor

import android.content.Context.MODE_PRIVATE
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.android_loop.ui.comentarios.ComentarioBurbuja
import com.example.android_loop.ui.comentarios.ComentariosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilVendedorScreen(
    vendedorId: Int,
    vendedorNombre: String,
    navController: NavController
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)
    val storedToken = prefs.getString("token", null)

    val comentariosViewModel: ComentariosViewModel = viewModel()
    var textoResena by remember { mutableStateOf("") }

    val comentarios = comentariosViewModel.comentarios
    val isLoading = comentariosViewModel.isLoading
    val errorMessage = comentariosViewModel.errorMessage
    val currentUser = comentariosViewModel.currentUserName
    val currentUserId = comentariosViewModel.currentUserId
    val enviado = comentariosViewModel.comentarioEnviado

    val esMiPerfil = currentUserId != 0 && currentUserId == vendedorId

    LaunchedEffect(Unit) {
        storedToken?.let { token ->
            comentariosViewModel.cargarUsuarioActual(token)
            comentariosViewModel.cargarComentarios(vendedorId)
        }
    }

    LaunchedEffect(enviado) {
        if (enviado) {
            textoResena = ""
            comentariosViewModel.resetComentarioEnviado()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(vendedorNombre) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                "Reseñas",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Box(modifier = Modifier.weight(1f)) {
                when {
                    isLoading && comentarios.isEmpty() -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color(0xFF003459))
                        }
                    }
                    errorMessage != null -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(errorMessage, color = Color.Red)
                        }
                    }
                    comentarios.isEmpty() -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Aún no hay reseñas", color = Color.Gray)
                        }
                    }
                    else -> {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(comentarios) { comentario ->
                                ComentarioBurbuja(
                                    comentario = comentario,
                                    esMio = comentario.comentador == currentUser
                                )
                            }
                        }
                    }
                }
            }

            if (!esMiPerfil) {
                Spacer(Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = textoResena,
                        onValueChange = { textoResena = it },
                        placeholder = { Text("Escribe una reseña...") },
                        modifier = Modifier.weight(1f),
                        maxLines = 3,
                        shape = RoundedCornerShape(20.dp)
                    )
                    IconButton(
                        onClick = { comentariosViewModel.enviarComentario(vendedorId, textoResena) },
                        enabled = textoResena.isNotBlank() && !isLoading
                    ) {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = "Enviar",
                            tint = if (textoResena.isNotBlank()) Color(0xFF003459) else Color.Gray
                        )
                    }
                }
            }
        }
    }
}
