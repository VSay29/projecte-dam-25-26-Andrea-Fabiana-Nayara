
package com.example.android_loop

import android.content.Context.MODE_PRIVATE
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class Usuario(
    val id: Int,
    val username: String,
    val image_1920: String
)

@Composable
fun PerfilUsuario(navController: NavHostController,) {

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)
    val storedToken = prefs.getString("token", null)
    var perfil by remember { mutableStateOf<Usuario?>(null) }
    var username by remember { mutableStateOf("") }
    var id by remember { mutableIntStateOf(0) }
    var image_1920 by remember { mutableStateOf("") }
    var avatarImage by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(Unit) {

        val response: RpcResponse<Usuario> = cliente.get("http://10.0.2.2:8069/api/v1/loop/me") {
            header("Authorization", "Bearer $storedToken")
            contentType(ContentType.Application.Json)
            setBody(
                buildJsonObject {
                    put("jsonrpc", "2.0")
                    put("method", "call")
                    put("params", buildJsonObject {})
                }
            )
        }.body()

        perfil = response.result
        username = response.result.username
        id = response.result.id
        image_1920 = response.result.image_1920 // Imagen en base64

        // conversion base64 a Image

        val decodedString = Base64.decode(image_1920, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        avatarImage = bitmap.asImageBitmap()

    }

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

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Texto a la izquierda
                Text(
                    "Información",
                    textAlign = TextAlign.Left,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif,
                    lineHeight = 50.sp,
                    modifier = Modifier.weight(1f).padding(8.dp)
                )

                // Imagen a la derecha
                avatarImage?.let { img ->
                    Image(
                        bitmap = img,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                }
            }


            Row () {
                // Información del usuario
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
                        Button (onClick = {}, Modifier.padding(bottom = 40.dp).fillMaxWidth(0.6f),
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

/*@Preview(showBackground = true)
@Composable
fun PerfilUsuarioPreview() {
    Android_LoopTheme {
        PerfilUsuario(navController)
    }
}*/