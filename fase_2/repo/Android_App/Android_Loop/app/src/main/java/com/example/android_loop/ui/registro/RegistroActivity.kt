package com.example.android_loop.ui.registro

import android.util.Log
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController


@Composable
fun Registro(navController: NavHostController) {

    val viewModelRegistro: RegistroViewModel = viewModel()
    val registroState = viewModelRegistro.registroState

    var name by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var passwd by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var errorName by remember { mutableStateOf(false) }
    var errorUsername by remember { mutableStateOf(false) }
    var errorPasswd by remember { mutableStateOf(false) }
    var errorEmail by remember { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Box(Modifier
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

            Row (Modifier.padding(top = 32.dp)) {
                Text(
                    "¡ÚNETE!", textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 40.sp,
                    fontFamily = FontFamily.SansSerif,
                    lineHeight = 50.sp,
                )
            }

            Row(Modifier.height(500.dp)) {
                Card(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(horizontal = 16.dp).padding(top = 24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp
                    )
                ) {
                    Box (Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {

                        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                            Row(Modifier.padding(bottom = 40.dp)) {
                                Text(
                                    "LOOP", textAlign = TextAlign.Center,
                                    color = Color(0xFF003459),
                                    fontSize = 40.sp,
                                    fontFamily = FontFamily.SansSerif,
                                    lineHeight = 50.sp,
                                )
                            }

                            Row()
                            {
                                OutlinedTextField(
                                    value = name,
                                    onValueChange = {
                                        name = it
                                        errorName = name.isEmpty()
                                    },
                                    label = { Text("Introduce tu nombre completo") },
                                    isError = errorName
                                )
                            }

                            Spacer(Modifier.height(8.dp))

                            Row()
                            {
                                OutlinedTextField(
                                    value = username,
                                    onValueChange = {
                                        username = it
                                        errorUsername = username.isEmpty()
                                    },
                                    label = { Text("Introduce tu nombre de usuario") },
                                    isError = errorUsername
                                )
                            }

                            Spacer(Modifier.height(8.dp))

                            Row {
                                OutlinedTextField(
                                    value = email,
                                    onValueChange = {
                                        email = it
                                        errorEmail = email.isEmpty()
                                    },
                                    label = { Text("Introduce el email") },
                                    isError = errorEmail,
                                )
                            }

                            Spacer(Modifier.height(8.dp))

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
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                )
                            }

                            Spacer(Modifier.height(32.dp))

                            Button(
                                onClick = {
                                    errorUsername = username.isEmpty()
                                    errorPasswd = passwd.isEmpty()
                                    errorEmail = email.isEmpty()

                                    if (!errorUsername && !errorEmail && !errorPasswd) viewModelRegistro.registro(name, username, email, passwd)

                                }, Modifier.padding(bottom = 30.dp).fillMaxWidth(0.6f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF003459),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("REGISTRARSE")
                            }

                            LaunchedEffect(registroState) {
                                registroState?.onSuccess { token ->
                                    // TODO: Lanzar mensaje de registro realizado y confirmación para volver al login

                                    navController.navigate("login")
                                }
                            }

                            registroState?.onFailure {
                                Text("Error en el registro")
                            }

                            Row {
                                Text(
                                    text = "Volver al login",
                                    Modifier.padding(bottom = 20.dp).clickable{navController.navigate("login")},
                                    textDecoration = TextDecoration.Underline,
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