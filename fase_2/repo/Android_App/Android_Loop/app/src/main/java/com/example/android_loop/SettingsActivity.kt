package com.example.android_loop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
                Text("Pantalla de ajustes")
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