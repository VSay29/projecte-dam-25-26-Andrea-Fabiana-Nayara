package com.example.android_loop.ui.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.android_loop.ui.perfilUsuario.PerfilUsuario
import com.example.android_loop.Producto.CreateProductScreen
import com.example.android_loop.Producto.ProductScreen
import com.example.android_loop.Producto.ProductViewModel
import com.example.android_loop.ui.registro.Registro

class MainActivity : ComponentActivity() {

    private val viewModelProductos by viewModels<ProductViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "login"
            ) {

                // Rutas de navegación

                composable("login") { Loggeo(navController) } // login
                composable("perfilUsuario") { PerfilUsuario(navController) } // perfil usuario
                composable("registro") { Registro(navController) } // registro
                composable("crear_producto") { // crear producto
                    CreateProductScreen(viewModelProductos, navController)
                }

                composable("pantalla_listado") { // pantalla listado productos
                    ProductScreen(viewModelProductos, navController)
                }
            }
    }
}

@Composable
fun Loggeo(navController: NavHostController) {

    val viewModelLogin: LoginViewModel = viewModel()
    val loginState = viewModelLogin.loginState

    var username by rememberSaveable { mutableStateOf("") }
    var passwd by rememberSaveable { mutableStateOf("") }
    var errorNombre by remember { mutableStateOf(false) }
    var errorPasswd by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // TODO: Al abrir app, se validará si hay token válido, de manera que el usuario no inicie sesión cada vez que abre la app

    Box(
        Modifier.Companion
            .fillMaxSize()
            .background(Color.Companion.Transparent)
    ) {
        Box(
            Modifier.Companion
                .size(830.dp)
                .align(Alignment.Companion.TopEnd)
                .offset(x = 10.dp, y = (-500).dp)
                .drawBehind {
                    drawCircle(
                        color = Color(0xFF003459),
                        radius = size.maxDimension * 0.5f,
                        center = Offset(
                            x = size.width * 0.8f,
                            y = size.height / 1.25f
                        )
                    )
                }
        )

        Column(
            Modifier.Companion
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Companion.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Row(Modifier.Companion.padding(top = 32.dp)) {
                Text(
                    "¡BIENVENIDO!", textAlign = TextAlign.Companion.Center,
                    color = Color.Companion.White,
                    fontSize = 40.sp,
                    fontFamily = FontFamily.Companion.SansSerif,
                    lineHeight = 50.sp,
                )
            }

            Row(Modifier.Companion.height(500.dp)) {
                Card(
                    modifier = Modifier.Companion.fillMaxWidth().fillMaxHeight()
                        .padding(horizontal = 16.dp).padding(top = 24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Companion.White
                    ),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp
                    )
                ) {
                    Box(
                        Modifier.Companion.fillMaxSize(),
                        contentAlignment = Alignment.Companion.BottomCenter
                    ) {

                        Column(
                            Modifier.Companion.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Companion.CenterHorizontally
                        ) {

                            Row(Modifier.Companion.padding(bottom = 40.dp)) {
                                Text(
                                    "LOOP", textAlign = TextAlign.Companion.Center,
                                    color = Color(0xFF003459),
                                    fontSize = 40.sp,
                                    fontFamily = FontFamily.Companion.SansSerif,
                                    lineHeight = 50.sp,
                                )
                            }

                            Row()
                            {
                                OutlinedTextField(
                                    value = username,
                                    onValueChange = {
                                        username = it
                                        errorNombre = username.isEmpty()
                                    },
                                    label = { Text("Introduce el nombre de usuario") },
                                    isError = errorNombre
                                )
                            }

                            Spacer(Modifier.Companion.height(8.dp))

                            Row {
                                OutlinedTextField(
                                    value = passwd,
                                    onValueChange = {
                                        passwd = it
                                        errorPasswd = passwd.isEmpty()
                                    },
                                    label = { Text("Introduce la contraseña") },
                                    isError = errorPasswd,
                                    visualTransformation = PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Companion.Password),
                                )
                            }

                            Spacer(Modifier.Companion.height(32.dp))

                            Button(
                                onClick = {
                                    errorNombre = username.isEmpty()
                                    errorPasswd = passwd.isEmpty()

                                    if (!errorNombre && !errorPasswd) viewModelLogin.login(username, passwd)

                                }, Modifier.Companion.padding(bottom = 5.dp).fillMaxWidth(0.6f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF003459),
                                    contentColor = Color.Companion.White
                                ),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                            ) {
                                Text("INICIAR SESIÓN")
                            }

                                LaunchedEffect(loginState) {
                                    loginState?.onSuccess { token ->
                                        val prefs = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)
                                        prefs.edit { putString("token", token) }

                                        navController.navigate("perfilUsuario")
                                    }
                                }

                                loginState?.onFailure {
                                    Text("Error en login")
                                }
                            }

                            Row {
                                Text(
                                    text = "Crear nueva cuenta",
                                    Modifier.Companion.padding(bottom = 20.dp).clickable {
                                        navController.navigate("registro")
                                    },
                                    textDecoration = TextDecoration.Companion.Underline,
                                    color = Color(0xFF003459)
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}