
package com.example.android_loop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_loop.ui.theme.Android_LoopTheme

class RegistroActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Registro()
        }
    }
}

@Composable
fun Registro() {
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
                        Button (onClick = {}, Modifier.padding(bottom = 40.dp).fillMaxWidth(0.6f),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF003459),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("REGISTRARSE")
                        }

                        Row {
                            Text(
                                text = "Volver al login",
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

@Preview(showBackground = true)
@Composable
fun RegistroPreview() {
    Android_LoopTheme {
        Registro()
    }
}