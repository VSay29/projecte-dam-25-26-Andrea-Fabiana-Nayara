package com.example.android_loop.view.vistas

import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import com.example.android_loop.view.componentes.Loading_Componente
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.android_loop.R
import com.example.android_loop.utils.navegacionConfig.ROUTES
import com.example.android_loop.view.componentes.Boton_Componente
import com.example.android_loop.viewModel.RegistroUiState
import com.example.android_loop.viewModel.RegistroViewModel

@Composable
fun Registro(navController: NavHostController) {

    val viewModelRegistro: RegistroViewModel = viewModel()
    val registroState = viewModelRegistro.registroState

    val context = LocalContext.current

    var name by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var passwd by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var errorName by remember { mutableStateOf(false) }
    var errorUsername by remember { mutableStateOf(false) }
    var errorPasswd by remember { mutableStateOf(false) }
    var errorEmail by remember { mutableStateOf(false) }

    val inputColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        errorContainerColor = Color.White,
        focusedBorderColor = Color(0xFF003459),
        unfocusedBorderColor = Color.Transparent,
        errorBorderColor = Color.Transparent,
    )

    Box(Modifier.fillMaxSize()) {

        // Fondo
        Image(
            painter = painterResource(id = R.drawable.fondo_registro),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo encima del card
            Image(
                painter = painterResource(id = R.drawable.loop_logo),
                contentDescription = null,
                modifier = Modifier.size(150.dp).offset(y = 18.dp)
            )

            // Card gris
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {

                        // Nombre completo
                        Column(Modifier.fillMaxWidth()) {
                            Text(
                                text = "Nombre completo",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.Black,
                                modifier = Modifier.padding(bottom = 6.dp, start = 2.dp)
                            )
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it; errorName = name.isEmpty() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(4.dp, if (errorName) Color(0xFFE57373) else Color(0xFFF5F5F5), RoundedCornerShape(50.dp)),
                                isError = errorName,
                                singleLine = true,
                                shape = RoundedCornerShape(50.dp),
                                colors = inputColors
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        // Usuario
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
                                onValueChange = { username = it; errorUsername = username.isEmpty() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(4.dp, if (errorUsername) Color(0xFFE57373) else Color(0xFFF5F5F5), RoundedCornerShape(50.dp)),
                                isError = errorUsername,
                                singleLine = true,
                                shape = RoundedCornerShape(50.dp),
                                colors = inputColors
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        // Email
                        Column(Modifier.fillMaxWidth()) {
                            Text(
                                text = "Email",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.Black,
                                modifier = Modifier.padding(bottom = 6.dp, start = 2.dp)
                            )
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it; errorEmail = email.isEmpty() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(4.dp, if (errorEmail) Color(0xFFE57373) else Color(0xFFF5F5F5), RoundedCornerShape(50.dp)),
                                isError = errorEmail,
                                singleLine = true,
                                shape = RoundedCornerShape(50.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                colors = inputColors
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        // Contraseña
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
                                onValueChange = { passwd = it; errorPasswd = passwd.isEmpty() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(4.dp, if (errorPasswd) Color(0xFFE57373) else Color(0xFFF5F5F5), RoundedCornerShape(50.dp)),
                                isError = errorPasswd,
                                singleLine = true,
                                shape = RoundedCornerShape(50.dp),
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                colors = inputColors
                            )
                        }

                        Spacer(Modifier.height(22.dp))

                        Boton_Componente(
                            texto = "Registrarse",
                            onClick = {
                                errorName = name.isEmpty()
                                errorUsername = username.isEmpty()
                                errorPasswd = passwd.isEmpty()
                                errorEmail = email.isEmpty()
                                if (!errorName && !errorUsername && !errorEmail && !errorPasswd) {
                                    viewModelRegistro.registro(name, username, email, passwd)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = name.isNotEmpty() && username.isNotEmpty() && email.isNotEmpty() && passwd.isNotEmpty()
                        )

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = "¿Ya tienes una cuenta?",
                            fontSize = 13.sp,
                            color = Color(0xFF555555)
                        )

                        Spacer(Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .border(1.5.dp, Color.Black, RoundedCornerShape(6.dp))
                                .clickable { navController.navigate(ROUTES.LOGIN) }
                                .padding(horizontal = 28.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "INICIA SESIÓN",
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

        LaunchedEffect(registroState) {
            if (registroState is RegistroUiState.Success) {
                Toast.makeText(context, "Registro realizado correctamente", Toast.LENGTH_SHORT).show()
                navController.navigate(ROUTES.LOGIN)
            }
            if (registroState is RegistroUiState.Error) {
                Log.d("DEBUG_REGISTRO", registroState.message)
                Toast.makeText(context, registroState.message, Toast.LENGTH_SHORT).show()
            }
        }

        Loading_Componente(visible = registroState is RegistroUiState.Loading)
    }
}
