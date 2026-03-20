//package com.example.android_loop.ui.comentarios
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Send
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ComentariosScreen(
//    productoId: Int,
//    navController: NavController,
//    viewModel: ComentariosViewModel = viewModel()
//) {
//    val comentarios = viewModel.comentarios
//    val isLoading = viewModel.isLoading
//    val error = viewModel.errorMessage
//    val enviado = viewModel.comentarioEnviado
//
//    var textoComentario by remember { mutableStateOf("") }
//
//    LaunchedEffect(productoId) {
//        viewModel.cargarComentarios(productoId)
//    }
//
//    LaunchedEffect(enviado) {
//        if (enviado) {
//            textoComentario = ""
//            viewModel.resetComentarioEnviado()
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Reseñas") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = Color(0xFF003459),
//                    titleContentColor = Color.White,
//                    navigationIconContentColor = Color.White
//                )
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//        ) {
//            when {
//                isLoading && comentarios.isEmpty() -> {
//                    Box(
//                        modifier = Modifier.weight(1f).fillMaxWidth(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        CircularProgressIndicator(color = Color(0xFF003459))
//                    }
//                }
//                error != null && comentarios.isEmpty() -> {
//                    Box(
//                        modifier = Modifier.weight(1f).fillMaxWidth(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text("Error al cargar las reseñas", color = MaterialTheme.colorScheme.error)
//                    }
//                }
//                comentarios.isEmpty() -> {
//                    Box(
//                        modifier = Modifier.weight(1f).fillMaxWidth(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text("Aún no hay reseñas. ¡Sé el primero!", color = Color.Gray)
//                    }
//                }
//                else -> {
//                    LazyColumn(
//                        modifier = Modifier.weight(1f),
//                        contentPadding = PaddingValues(16.dp),
//                        verticalArrangement = Arrangement.spacedBy(10.dp)
//                    ) {
//                        items(comentarios.filter { it.estado == "published" }) { comentario ->
//                            ComentarioItem(comentario)
//                        }
//                    }
//                }
//            }
//
//            HorizontalDivider()
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(12.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                OutlinedTextField(
//                    value = textoComentario,
//                    onValueChange = { textoComentario = it },
//                    placeholder = { Text("Escribe tu reseña...") },
//                    modifier = Modifier.weight(1f),
//                    maxLines = 3,
//                    shape = RoundedCornerShape(12.dp)
//                )
//                IconButton(
//                    onClick = { viewModel.enviarComentario(productoId, textoComentario) },
//                    enabled = textoComentario.isNotBlank() && !isLoading
//                ) {
//                    Icon(
//                        Icons.Default.Send,
//                        contentDescription = "Enviar",
//                        tint = if (textoComentario.isNotBlank()) Color(0xFF003459) else Color.Gray
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun ComentarioItem(comentario: Comentario) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(12.dp),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
//    ) {
//        Column(modifier = Modifier.padding(12.dp)) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(
//                    text = comentario.comentador,
//                    style = MaterialTheme.typography.labelMedium,
//                    color = Color(0xFF003459)
//                )
//                Text(
//                    text = comentario.fecha_creacion.take(10),
//                    style = MaterialTheme.typography.labelSmall,
//                    color = Color.Gray
//                )
//            }
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(
//                text = comentario.contenido,
//                style = MaterialTheme.typography.bodyMedium
//            )
//        }
//    }
//}