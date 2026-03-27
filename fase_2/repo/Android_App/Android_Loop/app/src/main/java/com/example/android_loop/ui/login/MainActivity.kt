package com.example.android_loop.ui.login

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.painterResource
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
import com.example.android_loop.R
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.auth0.jwt.JWT
import com.example.android_loop.ui.Producto.CreateProductScreen
import com.example.android_loop.data.Producto.ProductScreen
import com.example.android_loop.ui.Producto.DetalleProductoScreen
import com.example.android_loop.ui.Producto.ViewModel_Producto
import com.example.android_loop.ui.registro.Registro
import com.example.android_loop.ui.ajustes.SettingsScreen
import com.example.android_loop.ui.compra.Compra
import com.example.android_loop.ui.favoritos.Favoritos
import com.example.android_loop.ui.home.Home
import com.example.android_loop.ui.shoppingCart.CartScreen
import com.example.android_loop.ui.shoppingCart.CartViewModel
import com.example.android_loop.ui.theme.Android_LoopTheme
import com.example.android_loop.ui.theme.isDarkTheme
import java.security.MessageDigest
import java.util.Date
import kotlin.getValue

class MainActivity : ComponentActivity() {

    private val viewModelProductos by viewModels<ViewModel_Producto>()
    private val viewModelCart by viewModels<CartViewModel>()
    private val logo = R.drawable.loop_logo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val prefs = getSharedPreferences("loop_prefs", MODE_PRIVATE)
        isDarkTheme = prefs.getBoolean("dark_mode", false)
        setContent {
            Android_LoopTheme {
                val navController = rememberNavController()

                val context = LocalContext.current
                val prefs = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)
                val token = prefs.getString("token", "") ?: ""
                var rutaInicio = ""

                if (tokenValido(token)) rutaInicio = "home"
                else rutaInicio = "login"

                NavHost(
                    navController = navController,
                    startDestination = rutaInicio
                ) {

                    // Rutas de navegación

                    composable("login") { Loggeo(navController) } // login
                    composable("perfilUsuario") { PerfilUsuario(navController) } // perfil usuario
                    composable("favoritos") { Favoritos(navController) } // favoritos
                    composable("home") { Home(navController) } // home
                    composable("registro") { Registro(navController) } // registro
                    composable("pago") { Compra(navController, viewModelCart) }
                    composable("crear_producto") { // crear producto
                        val context = LocalContext.current
                        val token = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)
                            .getString("token", "") ?: ""
                        CreateProductScreen(token, viewModelProductos, navController)
                    }

                    composable("pantalla_listado") {
                        val context = LocalContext.current
                        val token = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)
                            .getString("token", "") ?: ""

                        ProductScreen(
                            token,
                            viewModel = viewModelProductos,
                            navController = navController,
                            cartViewModel = viewModelCart
                        )
                    }

                    composable(
                        route = "detalle_producto/{productId}",
                        arguments = listOf(navArgument("productId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val context = LocalContext.current
                        val token = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)
                            .getString("token", "") ?: ""
                        val productId =
                            backStackEntry.arguments?.getInt("productId") ?: return@composable
                        DetalleProductoScreen(
                            token,
                            productId = productId,
                            viewModel = viewModelProductos,
                            cartViewModel = viewModelCart,
                            navController = navController
                        )
                    }

                    composable("ajustes/{idioma}", arguments = listOf(navArgument("idioma") { type = NavType.StringType })
                    ) { backStackEntry -> // pantalla de ajustes
                        val idioma = backStackEntry.arguments!!.getString("idioma") ?: "Español"
                        SettingsScreen(navController, idioma = idioma)
                    }
                    composable("carrito") { // carrito de compra
                        CartScreen(viewModelCart, navController)
                    }

                }
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

    val logo = R.drawable.loop_logo

    val context = LocalContext.current

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Box(
            Modifier
                .size(830.dp)
                .align(Alignment.TopEnd)
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
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Row(Modifier.padding(top = 32.dp)) {
                Text(
                    "¡BIENVENIDO!", textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 40.sp,
                    fontFamily = FontFamily.SansSerif,
                    lineHeight = 50.sp,
                )
            }

            Row(Modifier.height(500.dp)) {
                Card(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight()
                        .padding(horizontal = 16.dp).padding(top = 24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp
                    )
                ) {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {

                        Column(
                            Modifier.fillMaxSize().padding(top = 24.dp),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Image(
                                painter = painterResource(id = logo),
                                contentDescription = null,
                                modifier = Modifier.size(200.dp)
                            )

                            Spacer(Modifier.height(16.dp))

                            OutlinedTextField(
                                value = username,
                                onValueChange = {
                                    username = it
                                    errorNombre = username.isEmpty()
                                },
                                label = { Text("Introduce el nombre de usuario") },
                                isError = errorNombre
                            )

                            Spacer(Modifier.height(8.dp))

                            OutlinedTextField(
                                value = passwd,
                                onValueChange = {
                                    passwd = it
                                    errorPasswd = passwd.isEmpty()
                                },
                                label = { Text("Introduce la contraseña") },
                                isError = errorPasswd,
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            )

                            Spacer(Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    errorNombre = username.isEmpty()
                                    errorPasswd = passwd.isEmpty()

                                    if (!errorNombre && !errorPasswd) viewModelLogin.login(username, encriptarPasswd(passwd))

                                }, Modifier.padding(bottom = 5.dp).fillMaxWidth(0.6f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF003459),
                                    contentColor = Color.White
                                ),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                            ) {
                                Text("INICIAR SESIÓN")
                            }

                                LaunchedEffect(loginState) {
                                    if (loginState is LoginUiState.Success) {
                                        val token = (loginState).token
                                        val prefs = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)
                                        prefs.edit { putString("token", token) }
                                        navController.navigate("home")
                                    }
                                }
                            }

                            Row {
                                Text(
                                    text = "Crear nueva cuenta",
                                    Modifier.padding(bottom = 20.dp).clickable {
                                        navController.navigate("registro")
                                    },
                                    textDecoration = TextDecoration.Underline,
                                    color = Color(0xFF003459)
                                )
                            }
                        }

                    }
                }
            }

            if (loginState is LoginUiState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.Blue
                    )
                }
            }

        }
    }

fun encriptarPasswd(passwd: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(passwd.toByteArray(Charsets.UTF_8))
    return hashBytes.fold("") { str, byte -> str + "%02x".format(byte) }
}

fun tokenValido(token: String): Boolean {

    if (token.isEmpty() || token == "") return false

    return try {
        val tokenDecodificado = JWT.decode(token)
        val exp = tokenDecodificado.expiresAt ?: return false
        exp.after(Date())
    } catch (e: Exception) {
        return false
    }
}
