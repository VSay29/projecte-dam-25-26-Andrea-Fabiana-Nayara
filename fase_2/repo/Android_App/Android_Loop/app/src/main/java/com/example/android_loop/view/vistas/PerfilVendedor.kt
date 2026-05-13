package com.example.android_loop.view.vistas

import android.content.Context.MODE_PRIVATE
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import com.example.android_loop.view.componentes.Busqueda_Componente
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import android.net.Uri
import android.widget.Toast
import androidx.navigation.NavController
import com.example.android_loop.R
import com.example.android_loop.data.model_dataClass.comentarioResult.Comentario
import com.example.android_loop.utils.NavigationCache
import com.example.android_loop.utils.sinAcentos
import com.example.android_loop.view.componentes.Header_Componente
import com.example.android_loop.view.vistas.En_Proceso_De_Revisar.ComentarioBurbuja
import com.example.android_loop.viewModel.ComentariosViewModel
import com.example.android_loop.viewModel.DenunciaUiState
import com.example.android_loop.viewModel.DenunciaViewModel
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

    var showDenunciaDialog by remember { mutableStateOf(false) }
    var motivoDenuncia by remember { mutableStateOf("") }
    var comentarioADenunciar by remember { mutableStateOf<Int?>(null) }
    val denunciaViewModel: DenunciaViewModel = viewModel()

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

    LaunchedEffect(denunciaViewModel.emitirDenunciaState) {
        when (denunciaViewModel.emitirDenunciaState) {
            is DenunciaUiState.SuccessEmitirDenunciaComentario ->
                Toast.makeText(context, "Denuncia enviada correctamente", Toast.LENGTH_SHORT).show()
            is DenunciaUiState.Error ->
                Toast.makeText(context, "Error al enviar la denuncia", Toast.LENGTH_SHORT).show()
            else -> {}
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(Modifier.fillMaxSize()) {

            Column(
                Modifier
                    .weight(1f)
                    .background(Color.White)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Header_Componente(
                    titulo = vendedorNombre,
                    onBack = { navController.popBackStack() }
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = profileBitmap,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .border(3.dp, Color(0xFFDDDDDD), CircleShape)
                    )
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
                                Busqueda_Componente(
                                    value = filtro,
                                    onValueChange = { filtro = it },
                                    placeholder = "Buscar producto"
                                )

                                val productosFiltrados = remember(viewmodelProducto.products, filtro) {
                                    if (filtro.isEmpty()) viewmodelProducto.products
                                    else viewmodelProducto.products.filter {
                                        it.nombre.sinAcentos().lowercase().contains(filtro.sinAcentos().lowercase())
                                    }
                                }

                                if (productosFiltrados.isEmpty()) {
                                    Text("No hay productos en venta")
                                } else {
                                    Spacer(Modifier.height(10.dp))
                                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        productosFiltrados.forEach { producto ->
                                            Card(
                                                modifier = Modifier.fillMaxWidth(),
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
                                                    Column(Modifier.weight(1f)) {
                                                        Text(
                                                            text = producto.nombre,
                                                            fontWeight = FontWeight.SemiBold,
                                                            fontSize = 15.sp
                                                        )
                                                        Text(
                                                            text = producto.descripcion,
                                                            fontSize = 12.sp,
                                                            color = Color.Gray,
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis
                                                        )
                                                        if (producto.ubicacion.isNotBlank()) {
                                                            Text(
                                                                text = producto.ubicacion,
                                                                fontSize = 12.sp,
                                                                color = Color.Gray
                                                            )
                                                        }
                                                    }
                                                    Text(
                                                        text = "${producto.precio} €",
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFF003459),
                                                        fontSize = 15.sp
                                                    )
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
                                                    comentarioADenunciar = comentario.id
                                                    showDenunciaDialog = true
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
                        if (editandoComentario != null) {
                            Text(
                                text = "Editando reseña",
                                fontSize = 12.sp,
                                color = Color(0xFF003459)
                            )
                        }
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

    if (showDenunciaDialog) {
        AlertDialog(
            onDismissRequest = { showDenunciaDialog = false; motivoDenuncia = "" },
            title = { Text("Denunciar reseña") },
            text = {
                Column {
                    Text("Describe el motivo de la denuncia:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = motivoDenuncia,
                        onValueChange = { motivoDenuncia = it },
                        placeholder = { Text("Motivo...") },
                        maxLines = 4,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        comentarioADenunciar?.let { id ->
                            storedToken?.let { token ->
                                denunciaViewModel.emitirDenunciaComentario(token, id, motivoDenuncia)
                            }
                        }
                        showDenunciaDialog = false
                        motivoDenuncia = ""
                        comentarioADenunciar = null
                    },
                    enabled = motivoDenuncia.isNotBlank()
                ) { Text("Denunciar") }
            },
            dismissButton = {
                TextButton(onClick = { showDenunciaDialog = false; motivoDenuncia = "" }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
