package com.example.android_loop.view.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tuapp.ui.theme.OnPrimary
import com.tuapp.ui.theme.Primary
import com.tuapp.ui.theme.Secondary

// Ejemplo de uso: Boton_componente(texto = "Guardar Producto", onClick = { ... })
@Composable
fun Boton_Componente(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,  // Modifier por defecto vacío, se puede personalizar al usarlo
    enabled: Boolean
) {
    val forma = RoundedCornerShape(50.dp) // Forma de "píldora" — esquinas muy redondeadas

    val colores = if (enabled) listOf(Primary, Secondary)
    else listOf(Primary.copy(alpha = 0.55f), Secondary.copy(alpha = 0.55f))

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .shadow(elevation = if (enabled) 6.dp else 0.dp, shape = forma),
        shape = forma,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = OnPrimary,
        ),
        contentPadding = PaddingValues(0.dp),
        enabled = enabled
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(brush = Brush.horizontalGradient(colores)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = texto,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = OnPrimary
            )
        }
    }
}
