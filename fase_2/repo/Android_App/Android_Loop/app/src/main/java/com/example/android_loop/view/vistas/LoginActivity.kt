package com.example.android_loop.view.vistas

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.android_loop.R
import com.example.android_loop.utils.navegacionConfig.ROUTES
import com.example.android_loop.utils.setToken
import com.example.android_loop.view.componentes.BotonCrearProducto
import com.example.android_loop.view.theme.Android_LoopTheme
import com.example.android_loop.viewModel.LoginUiState
import com.example.android_loop.viewModel.LoginViewModel

@Composable
fun Loggeo(navController: NavHostController) {

    val viewModelLogin: LoginViewModel = viewModel()
    val loginState = viewModelLogin.loginState

    var username by rememberSaveable { mutableStateOf("") }
    var passwd by rememberSaveable { mutableStateOf("") }
    var errorNombre by remember { mutableStateOf(false) }
    var errorPasswd by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Box(Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.fondo_login),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.loop_logo),
                contentDescription = null,
                modifier = Modifier.size(150.dp).offset(y = 18.dp)
            )

            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFD6D6D6).copy(alpha = 0.65f),
                            shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp)
                        )
                        .padding(top = 28.dp, start = 22.dp, end = 22.dp, bottom = 36.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {


                        Column(Modifier.fillMaxWidth()) {
                            Text(
                                text = "Usuario",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.Black,
                                modifier = Modifier.padding(bottom = 6.dp, start = 2.dp)
                            )
                            OutlinedTextField(
                                value = username,
                                onValueChange = {
                                    username = it
                                    errorNombre = username.isEmpty()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(4.dp, if (errorNombre) Color(0xFFE57373) else Color(0xFFF5F5F5), RoundedCornerShape(50.dp)),
                                isError = errorNombre,
                                singleLine = true,
                                shape = RoundedCornerShape(50.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    errorContainerColor = Color.White,
                                    focusedBorderColor = Color(0xFF003459),
                                    unfocusedBorderColor = Color.Transparent,
                                    errorBorderColor = Color.Transparent,
                                )
                            )
                        }

                        Spacer(Modifier.height(14.dp))

                        Column(Modifier.fillMaxWidth()) {
                            Text(
                                text = "Contraseña",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.Black,
                                modifier = Modifier.padding(bottom = 6.dp, start = 2.dp)
                            )
                            OutlinedTextField(
                                value = passwd,
                                onValueChange = {
                                    passwd = it
                                    errorPasswd = passwd.isEmpty()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(4.dp, if (errorPasswd) Color(0xFFE57373) else Color(0xFFF5F5F5), RoundedCornerShape(50.dp)),
                                isError = errorPasswd,
                                singleLine = true,
                                shape = RoundedCornerShape(50.dp),
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    errorContainerColor = Color.White,
                                    focusedBorderColor = Color(0xFF003459),
                                    unfocusedBorderColor = Color.Transparent,
                                    errorBorderColor = Color.Transparent,
                                )
                            )
                        }

                        Spacer(Modifier.height(22.dp))

                        BotonCrearProducto(
                            texto = "Iniciar Sesión",
                            onClick = {
                                errorNombre = username.isEmpty()
                                errorPasswd = passwd.isEmpty()
                                if (!errorNombre && !errorPasswd) {
                                    viewModelLogin.login(username, passwd)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = username.isNotEmpty() && passwd.isNotEmpty()
                        )

                        Spacer(Modifier.height(70.dp))

                        Text(
                            text = "¿No tienes una cuenta?",
                            fontSize = 13.sp,
                            color = Color(0xFF555555)
                        )

                        Spacer(Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .border(
                                    width = 1.5.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .clickable { navController.navigate(ROUTES.REGISTRO) }
                                .padding(horizontal = 28.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "REGÍSTRATE",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
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

        if (loginState is LoginUiState.Loading || loginState is LoginUiState.Success) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF003459))
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
