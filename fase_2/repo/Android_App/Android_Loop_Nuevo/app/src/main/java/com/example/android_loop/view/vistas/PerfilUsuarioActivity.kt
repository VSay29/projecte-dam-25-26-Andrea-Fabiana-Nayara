package com.example.android_loop.view.vistas

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.android_loop.R
import com.example.android_loop.utils.base64ToImage
import com.example.android_loop.utils.getToken
import com.example.android_loop.utils.sinAcentos
import com.example.android_loop.utils.toBase64
import com.example.android_loop.view.theme.Android_LoopTheme
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
    val defaultAvatar = ImageBitmap.imageResource(R.drawable.no_avatar)
    var avatarImage by remember { mutableStateOf<ImageBitmap?>(defaultAvatar) }

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
                .verticalScroll(rememberScrollState())
                .padding(bottom = 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(top = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                // -- BOTÓN AJUSTES --
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopEnd),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { navController.navigate("ajustes/$idioma") }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Ajustes",
                            tint = Color(0xFF003459),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // -- FOTO DE PERFIL EDITABLE --

                Column(Modifier.verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,) {
                    avatarImage?.let { img ->
                        Image(
                            bitmap = img,
                            contentDescription = null,
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                        )
                    }

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

                    if (isLoading) CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))

                    // -- BOTÓN LÁPIZ CAMBIAR FOTO DE PERFIL --

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Cambiar imagen",
                            tint = Color(0xFF003459),
                            modifier = Modifier.size(16.dp).clickable(onClick = {
                                launcher.launch("image/*")
                            })
                        )
                    }

                    Spacer(Modifier.Companion.height(8.dp))

                    Text(username)

                }

            }

            Spacer(Modifier.Companion.height(30.dp))

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
                                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(30.dp)),
                                value = filtro,
                                onValueChange = {
                                    filtro = it
                                },
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
                                                    esMio = comentario.comentador == comentariosViewModel.currentUserName
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