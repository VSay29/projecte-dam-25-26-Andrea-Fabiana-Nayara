package com.example.android_loop

import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android_loop.ui.theme.Android_LoopTheme

class SettingsActivity {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SettingsScreen() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Ajustes") }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    "Pantalla de ajustes",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                OptionsLazyColumn()
            }
        }
    }

    @Composable
    fun OptionsLazyColumn() {
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Card(
                    //modifier = Modifier.fillMaxSize(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Opción 1")
                        }
                        HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Opción 2")
                        }
                        HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Opción 3")
                        }
                        HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Opción 4")
                        }
                        HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Opción 5")
                        }
                        HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Opción 6")
                        }
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        Android_LoopTheme {
            SettingsScreen()
        }
    }
}