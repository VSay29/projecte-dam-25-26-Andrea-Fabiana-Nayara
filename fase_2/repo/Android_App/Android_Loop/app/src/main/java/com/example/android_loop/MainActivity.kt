
package com.example.android_loop

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_loop.ui.theme.Android_LoopTheme
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.client.request.post
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import androidx.core.content.edit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Loggeo()
        }
    }
}

// Configuración API KTOR

val cliente = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json{
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
}

@Serializable
data class RpcResponse(
    val result: LoginResult? = null,
    val error: RpcError? = null,
)

@Serializable
data class LoginResult(
    val token: String,
)

@Serializable
data class RpcError(
    val message: String? = null,
)


@SuppressLint("UseKtx")
@Composable
fun Loggeo() {

    val coroutineScope = rememberCoroutineScope()
    var token by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var passwd by rememberSaveable { mutableStateOf("") }
    var errorNombre by remember { mutableStateOf(false) }
    var errorPasswd by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Box(Modifier
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
                    "¡BIENVENIDO!", textAlign = TextAlign.Center,
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
                                    value = username,
                                    onValueChange = {
                                        username = it
                                        errorNombre = username.isEmpty()
                                    },
                                    label = { Text("Introduce el nombre de usuario") },
                                    isError = errorNombre
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
                                    errorNombre = username.isEmpty()
                                    errorPasswd = passwd.isEmpty()

                                    if (!errorNombre && !errorPasswd) {
                                        coroutineScope.launch {
                                            try {

                                                // Obtener token

                                                val response: RpcResponse = cliente.post("http://10.0.2.2:8069/api/v1/loop/auth") {
                                                    contentType(ContentType.Application.Json)
                                                    setBody(
                                                        buildJsonObject {
                                                            put("jsonrpc", "2.0")
                                                            put("method", "call")
                                                            put("params", buildJsonObject {
                                                                put("username", username)
                                                                put("password", passwd)
                                                            })
                                                        }
                                                    )
                                                }.body()

                                                response.result?.let {
                                                    token = it.token
                                                }

                                                // Almacenar token

                                                if (!token.isEmpty()) {

                                                    val prefs = context.getSharedPreferences(
                                                        "loop_prefs",
                                                        MODE_PRIVATE
                                                    )
                                                    prefs.edit { putString("token", token) }

                                                    // Obtener el token más tarde
                                                    //val storedToken = prefs.getString("token", null)
                                                    //Log.d("Token guardado", storedToken.toString())

                                                }

                                                /*
                                                * Recuperar token más tarde:
                                                * val storedToken = prefs.getString("token", null)
                                                * */

                                            } catch (e: Exception) {
                                                Log.e("LOGIN", "Error: ${e.message}")
                                            }
                                        }
                                    }

                                }, Modifier.padding(bottom = 30.dp).fillMaxWidth(0.6f),
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF003459),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("INICIAR SESIÓN")
                            }

                            Row {
                                Text(
                                    text = "Crear nueva cuenta",
                                    Modifier.padding(bottom = 20.dp),
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

@Preview(showBackground = true)
@Composable
fun LoggeoPreview() {
    Android_LoopTheme {
        Loggeo()
    }
}