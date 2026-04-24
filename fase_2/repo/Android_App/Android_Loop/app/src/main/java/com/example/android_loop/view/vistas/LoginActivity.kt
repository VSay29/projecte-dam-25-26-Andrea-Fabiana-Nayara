package com.example.android_loop.view.vistas

import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.snapshotFlow
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.android_loop.R
import com.example.android_loop.utils.getToken
import com.example.android_loop.utils.navegacionConfig.ROUTES
import com.example.android_loop.utils.setToken
import com.example.android_loop.view.theme.Android_LoopTheme
import com.example.android_loop.viewModel.LoginUiState
import com.example.android_loop.viewModel.LoginViewModel

@Composable
fun Loggeo(navController: NavHostController) {

    val logo = R.drawable.loop_logo

    val viewModelLogin: LoginViewModel = viewModel()
    val loginState = viewModelLogin.loginState

    var username by rememberSaveable { mutableStateOf("") }
    var passwd by rememberSaveable { mutableStateOf("") }
    var errorNombre by remember { mutableStateOf(false) }
    var errorPasswd by remember { mutableStateOf(false) }

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

                                    if (!errorNombre && !errorPasswd) viewModelLogin.login(username, passwd)

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
                                when (loginState) {
                                    is LoginUiState.Success -> {
                                        val token = loginState.token
                                        setToken(context, token)

                                        kotlinx.coroutines.delay(1000)

                                        navController.navigate(ROUTES.HOME)
                                    }
                                    is LoginUiState.Error -> {
                                        Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                                    }
                                    else -> {}
                                }
                            }
                        }

                        Row {
                            Text(
                                text = "Crear nueva cuenta",
                                Modifier.padding(bottom = 20.dp).clickable {
                                    navController.navigate(ROUTES.REGISTRO)
                                },
                                textDecoration = TextDecoration.Underline,
                                color = Color(0xFF003459)
                            )
                        }
                    }
                }
            }
        }



        if (loginState is LoginUiState.Loading || loginState is LoginUiState.Success) {
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

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    Android_LoopTheme {
        Loggeo(navController = rememberNavController())
    }
}

