package com.example.android_loop.view.vistas

import android.content.Context.MODE_PRIVATE
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import android.net.Uri
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.IconButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.android_loop.R
import com.example.android_loop.utils.base64ToImage
import com.example.android_loop.utils.getToken
import com.example.android_loop.utils.navegacionConfig.ROUTES
import com.example.android_loop.utils.sinAcentos
import com.example.android_loop.utils.toBase64
import com.example.android_loop.view.componentes.PantallaHeader
import com.example.android_loop.view.theme.Android_LoopTheme
import com.tuapp.ui.theme.OnPrimary
import com.example.android_loop.view.vistas.En_Proceso_De_Revisar.ComentarioBurbuja
import com.example.android_loop.viewModel.ComentarioUiState
import com.example.android_loop.viewModel.ComentariosViewModel
import com.example.android_loop.viewModel.PerfilUiState
import com.example.android_loop.viewModel.PerfilViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun PerfilUsuario(navController: NavHostController) {

    val context = LocalContext.current
    val token = getToken(context)
    val prefs = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)

    val perfilViewModel: PerfilViewModel = viewModel()
    val perfilState = perfilViewModel.perfilUiState

    val comentariosViewModel: ComentariosViewModel = viewModel()
    val comentarioUiState = comentariosViewModel.comentarioUiState

    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    var idUsuario by remember { mutableIntStateOf(0) }
    var username by remember { mutableStateOf("") }
    var idioma by remember { mutableStateOf("Español") }
    var image_1920 by remember { mutableStateOf("") }
    var avatarImage by remember { mutableStateOf<ImageBitmap?>(null) }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("En venta", "Reseñas")

    val productosState = perfilViewModel.perfilUiState
    var filtro by rememberSaveable { mutableStateOf("") }


    LaunchedEffect(Unit) {
        perfilViewModel.getUserData(token)
    }

    LaunchedEffect(perfilState) {

        when (val user = perfilState) {
            is PerfilUiState.SuccessGetUserData -> {

                // Cargar los datos del usuario

                idUsuario = user.resp.id
                username = user.resp.username
                image_1920 = user.resp.image_1920
                idioma = user.resp.idioma
                prefs.edit {
                    putString("IDIOMA", idioma)
                }

                // Pintar la foto de perfil

                if (image_1920.isNotBlank() && image_1920 != "false") avatarImage = withContext(Dispatchers.IO) { base64ToImage(image_1920) }

                // Cargar los comentarios/reseñas del usuario y los productos

                comentariosViewModel.cargarComentarios(token, idUsuario)
                perfilViewModel.cargarProductosUsuario(token, idUsuario)

            }

            is PerfilUiState.Error -> {
                Toast.makeText(context, user.message, Toast.LENGTH_SHORT).show()
            }

            else -> {}

        }
    }



    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            // -- HEADER CON NOMBRE Y AJUSTES --
            Box(Modifier.fillMaxWidth()) {
                PantallaHeader(titulo = username.ifBlank { "Mi perfil" }.replaceFirstChar { it.uppercase() })
                Row(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(end = 8.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    IconButton(onClick = { navController.navigate(ROUTES.AJUSTES) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Ajustes",
                            tint = OnPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // -- FOTO DE PERFIL EDITABLE --
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.GetContent()
                    ) { uri ->
                        uri?.let {
                            val nuevaFoto = toBase64(context, it)
                            isLoading = true
                            scope.launch {
                                perfilViewModel.cambiarFotoPerfil(getToken(context), nuevaFoto)
                                avatarImage = base64ToImage(nuevaFoto)
                                delay(1000)
                                isLoading = false
                                Toast.makeText(context, "¡Foto de perfil actualizada!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    Box(contentAlignment = Alignment.Center) {
                        if (avatarImage != null) {
                            Image(
                                bitmap = avatarImage!!,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(130.dp)
                                    .clip(CircleShape)
                                    .border(3.dp, Color(0xFFDDDDDD), CircleShape)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(130.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE8EEF4))
                                    .border(3.dp, Color(0xFFDDDDDD), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color(0xFF9E9E9E),
                                    modifier = Modifier.size(76.dp)
                                )
                            }
                        }

                        // Botón editar moderno
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF003459))
                                .clickable { launcher.launch("image/*") }
                                .align(Alignment.BottomEnd),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Cambiar imagen",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    if (isLoading) CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                }
            }

            Spacer(Modifier.height(30.dp))

            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
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
                                text = { Text(
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
                                    .background(
                                        Color.White
                                    )
                                    .padding(vertical = 8.dp, horizontal = 20.dp)
                                    .clip(RoundedCornerShape(30.dp))
                                    .let {
                                        if (selectedTab == index) {
                                            it.clip(RoundedCornerShape(30.dp))
                                        } else it
                                    }
                            )
                        }
                    }

                    when (selectedTab) {
                        0 -> {
                            TextField(
                                value = filtro,
                                onValueChange = { filtro = it },
                                placeholder = { Text("Buscar producto") },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(R.drawable.lupa),
                                        contentDescription = "Buscar",
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(30.dp)),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFF0F4F8),
                                    unfocusedContainerColor = Color(0xFFF0F4F8),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                )
                            )

                            // MOSTRAR LISTA DE PRODUCTOS DEL USUARIO

                            when(productosState) {

                                is PerfilUiState.Loading -> {
                                    CircularProgressIndicator()
                                }

                                is PerfilUiState.SuccessCargarProductoUsuario -> {
                                    val productosFiltrados = remember(productosState, filtro) {
                                        if(filtro.isEmpty()) productosState.resp
                                        else productosState.resp.filter {
                                            it.nombre.sinAcentos().lowercase().contains(filtro.sinAcentos().lowercase())
                                        }
                                    }

                                    if(!productosFiltrados.isEmpty()) {
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

                                else -> { Text("No hay productos en venta") }

                            }

                        }
                        1 -> {
                            var textoResena by remember { mutableStateOf("") }
                            val enviado = comentariosViewModel.comentarioEnviado

                            LaunchedEffect(enviado) {
                                if (enviado) {
                                    textoResena = ""
                                    comentariosViewModel.resetComentarioEnviado()
                                }
                            }

                            when (comentarioUiState) {
                                is ComentarioUiState.Loading -> {
                                    CircularProgressIndicator()
                                }

                                is ComentarioUiState.SuccessCargarComentarios -> {
                                    val comentarios = comentarioUiState.resp

                                    if (comentarios.isEmpty()) {
                                        Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                                            Text("Aún no hay reseñas", color = Color.Gray)
                                        }
                                    } else {
                                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            comentarios.forEach { comentario ->
                                                ComentarioBurbuja(
                                                    comentario = comentario,
                                                    esMio = comentario.comentador == comentariosViewModel.currentUserName,
                                                    onPerfilClick = { id, nombre ->
                                                        navController.navigate("${ROUTES.PERFIL_VENDEDOR}/$id/${Uri.encode(nombre)}")
                                                    },
                                                )
                                            }
                                        }
                                    }
                                }

                                is ComentarioUiState.Error -> {
                                    Text(comentarioUiState.message)
                                }

                                else -> {}

                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PerfilUsuarioPreview() {
    Android_LoopTheme {
        PerfilUsuario(navController = rememberNavController())
    }
}