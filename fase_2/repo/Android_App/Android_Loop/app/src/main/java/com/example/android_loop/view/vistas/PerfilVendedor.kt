package com.example.android_loop.view.vistas

import android.content.Context.MODE_PRIVATE
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import android.net.Uri
import androidx.navigation.NavController
import com.example.android_loop.R
import com.example.android_loop.data.model_dataClass.comentarioResult.Comentario
import com.example.android_loop.utils.NavigationCache
import com.example.android_loop.utils.sinAcentos
import com.example.android_loop.view.vistas.En_Proceso_De_Revisar.ComentarioBurbuja
import com.example.android_loop.viewModel.ComentariosViewModel
import com.example.android_loop.viewModel.PerfilViewModel

@Composable
fun PerfilVendedor(
    vendedorId: Int,
    vendedorNombre: String,
    navController: NavController
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)
    val storedToken = prefs.getString("token", null)

    val comentariosViewModel: ComentariosViewModel = viewModel()
    val viewmodelProducto: PerfilViewModel = viewModel()

    val defaultAvatar = ImageBitmap.imageResource(R.drawable.no_avatar)

    val profileBitmap: ImageBitmap = remember {
        val cached = NavigationCache.profileImage
        NavigationCache.profileImage = null
        cached
            ?.takeIf { it.isNotBlank() && it != "false" }
            ?.let {
                try {
                    val bytes = Base64.decode(it, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
                } catch (e: Exception) { null }
            } ?: defaultAvatar
    }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("En venta", "Reseñas")

    var filtro by rememberSaveable { mutableStateOf("") }
    var textoResena by remember { mutableStateOf("") }
    var estrellasSeleccionadas by remember { mutableIntStateOf(0) }
    var editandoComentario by remember { mutableStateOf<Comentario?>(null) }

    val comentarios = comentariosViewModel.comentarios
    val isLoading = comentariosViewModel.isLoading
    val currentUser = comentariosViewModel.currentUserName
    val currentUserId = comentariosViewModel.currentUserId
    val enviado = comentariosViewModel.comentarioEnviado

    val esMiPerfil = currentUserId != 0 && currentUserId == vendedorId
    val yaDejoResena = currentUserId != 0 && comentarios.any { it.comentador_partner_id == currentUserId }
    val mostrarFormulario = !yaDejoResena || editandoComentario != null

    LaunchedEffect(Unit) {
        storedToken?.let { token ->
            comentariosViewModel.cargarUsuarioActual(token)
            comentariosViewModel.cargarComentarios(storedToken, vendedorId)
        }
    }

    LaunchedEffect(enviado) {
        if (enviado) {
            textoResena = ""
            comentariosViewModel.resetComentarioEnviado()
        }
    }

    LaunchedEffect(storedToken) {
        if (storedToken != null) {
            viewmodelProducto.loadProductosUsuario(storedToken, vendedorId)
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Column(Modifier.fillMaxSize()) {

            // Contenido desplazable
            Column(
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopStart),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color(0xFF003459),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            bitmap = profileBitmap,
                            contentDescription = null,
                            modifier = Modifier.size(110.dp).clip(CircleShape)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(vendedorNombre)
                    }
                }

                Spacer(Modifier.height(30.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        TabRow(
                            selectedTabIndex = selectedTab,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            indicator = { tabPositions ->
                                Box(
                                    Modifier
                                        .tabIndicatorOffset(tabPositions[selectedTab])
                                        .height(4.dp)
                                        .background(Color(0xFF003459))
                                )
                            }
                        ) {
                            tabs.forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedTab == index,
                                    onClick = { selectedTab = index },
                                    text = {
                                        Text(
                                            title,
                                            color = Color(0xFF003459),
                                            fontSize = 14.sp,
                                            fontFamily = FontFamily.SansSerif,
                                            textAlign = TextAlign.Center,
                                            style = TextStyle(textDecoration = TextDecoration.None),
                                            maxLines = 1,
                                            overflow = TextOverflow.Visible,
                                        )
                                    },
                                    modifier = Modifier
                                        .background(Color.White)
                                        .padding(vertical = 8.dp, horizontal = 20.dp)
                                        .clip(RoundedCornerShape(30.dp))
                                        .let {
                                            if (selectedTab == index) it.clip(RoundedCornerShape(30.dp))
                                            else it
                                        }
                                )
                            }
                        }

                        when (selectedTab) {
                            0 -> {
                                TextField(
                                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(30.dp)),
                                    value = filtro,
                                    onValueChange = { filtro = it },
                                    placeholder = { Text("Buscar producto") },
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(R.drawable.lupa),
                                            contentDescription = "Buscar",
                                            modifier = Modifier.size(24.dp)
                                        )
                                    },
                                    singleLine = true,
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.LightGray,
                                        unfocusedContainerColor = Color.LightGray,
                                        disabledContainerColor = Color.LightGray,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )
                                //TODO: MOSTRAR LISTA DE PRODUCTOS DEL USUARIO

                                val productosFiltrados = remember(viewmodelProducto.products, filtro) {
                                    if(filtro.isEmpty()) viewmodelProducto.products
                                    else viewmodelProducto.products.filter {
                                        it.nombre.sinAcentos().lowercase().contains(filtro.sinAcentos().lowercase())
                                    }
                                }

                                if(productosFiltrados.isEmpty()) {
                                    Text("No hay productos en venta")
                                } else {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        productosFiltrados.forEach { producto ->
                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(10.dp),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Box(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                                                    Column {
                                                        Text(text = producto.nombre)
                                                        Text(text = producto.descripcion)
                                                        Text(text = "Precio: ${producto.precio}")
                                                        Text(text = producto.ubicacion)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            1 -> {
                                if (isLoading && comentarios.isEmpty()) {
                                    Box(
                                        Modifier.fillMaxWidth().padding(32.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(color = Color(0xFF003459))
                                    }
                                } else if (comentarios.isEmpty()) {
                                    Box(
                                        Modifier.fillMaxWidth().padding(32.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Aún no hay reseñas", color = Color.Gray)
                                    }
                                } else {
                                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                        comentarios.forEach { comentario ->
                                            val esMioEsteComentario = comentario.comentador_partner_id == currentUserId
                                            ComentarioBurbuja(
                                                comentario = comentario,
                                                esMio = esMioEsteComentario,
                                                onPerfilClick = { id, nombre ->
                                                    navController.navigate("perfil_Vendedor/$id/${Uri.encode(nombre)}")
                                                },
                                                onDelete = if (esMioEsteComentario) {{
                                                    comentariosViewModel.eliminarComentario(storedToken!!, comentario.id, vendedorId)
                                                    editandoComentario = null
                                                    textoResena = ""
                                                    estrellasSeleccionadas = 0
                                                }} else null,
                                                onEdit = if (esMioEsteComentario) {{
                                                    editandoComentario = comentario
                                                    textoResena = comentario.contenido
                                                    estrellasSeleccionadas = comentario.valoracion?.toInt() ?: 0
                                                }} else null,
                                                onReport = if (!esMioEsteComentario) {{
                                                    // TODO: implementar denuncias
                                                }} else null
                                            )
                                        }
                                    }
                                }

                                comentariosViewModel.errorMessage?.let { error ->
                                    Spacer(Modifier.height(8.dp))
                                    Text(error, color = Color.Red, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Input fijo en la parte inferior (solo tab Reseñas y si no es mi perfil)
            if (selectedTab == 1 && !esMiPerfil) {
                HorizontalDivider()
                if (!mostrarFormulario) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Ya has dejado una reseña a este usuario",
                            color = Color.Gray,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Etiqueta cuando se está editando
                        if (editandoComentario != null) {
                            Text(
                                text = "Editando reseña",
                                fontSize = 12.sp,
                                color = Color(0xFF003459)
                            )
                        }
                        // Selector de estrellas
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            (1..5).forEach { star ->
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "$star estrellas",
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clickable { estrellasSeleccionadas = star },
                                    tint = if (star <= estrellasSeleccionadas) Color(0xFFFFB800)
                                    else Color.LightGray
                                )
                            }
                        }
                        // Campo de texto + botón enviar
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
                                onClick = {
                                    val valoracion = if (estrellasSeleccionadas > 0) estrellasSeleccionadas.toFloat() else null
                                    val comentarioEditado = editandoComentario
                                    if (comentarioEditado != null) {
                                        comentariosViewModel.editarComentario(storedToken!!, comentarioEditado.id, textoResena, valoracion, vendedorId)
                                        editandoComentario = null
                                        textoResena = ""
                                        estrellasSeleccionadas = 0
                                    } else {
                                        comentariosViewModel.enviarComentario(storedToken!!, vendedorId, textoResena, valoracion)
                                    }
                                },
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
    }
}