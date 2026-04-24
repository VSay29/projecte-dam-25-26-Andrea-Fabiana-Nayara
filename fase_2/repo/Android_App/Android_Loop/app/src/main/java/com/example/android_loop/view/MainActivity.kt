package com.example.android_loop.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.android_loop.utils.navegacionConfig.AppNavGraph
import com.example.android_loop.view.theme.Android_LoopTheme
import com.example.android_loop.view.theme.isDarkTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val prefs = getSharedPreferences("loop_prefs", MODE_PRIVATE)
        isDarkTheme = prefs.getBoolean("dark_mode", false)
        setContent {
            Android_LoopTheme {
                val navController = rememberNavController()
                AppNavGraph(navController)
            }
        }
    }
}