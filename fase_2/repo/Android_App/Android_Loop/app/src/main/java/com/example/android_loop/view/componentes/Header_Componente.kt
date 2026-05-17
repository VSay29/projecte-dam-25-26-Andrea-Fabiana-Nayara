package com.example.android_loop.view.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.tuapp.ui.theme.OnPrimary
import com.tuapp.ui.theme.Primary
import com.tuapp.ui.theme.Secondary

// Componente reutilizable para el encabezado de cualquier pantalla.
// Recibe "titulo" como parámetro, así cada pantalla puede tener su propio texto.
// Ejemplo de uso: PantallaHeader(titulo = "Crear Producto")
@Composable
fun Header_Componente(titulo: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Primary, Secondary)
                )
            )
            .padding(top = 48.dp, bottom = 16.dp, start = 20.dp, end = 20.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleLarge,
            color = OnPrimary
        )
    }
}
