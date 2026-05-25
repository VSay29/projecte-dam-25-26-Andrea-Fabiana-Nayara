package com.example.android_loop.view.vistas.En_Proceso_De_Revisar

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_loop.data.model_dataClass.comentarioResult.Comentario
import com.example.android_loop.utils.NavigationCache

@Composable
fun ComentarioBurbuja(
    comentario: Comentario,
    esMio: Boolean,
    onPerfilClick: ((Int, String) -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    onEdit: (() -> Unit)? = null,
    onReport: (() -> Unit)? = null
) {
    val avatarBitmap = remember(comentario.imagen_comentador) {
        comentario.imagen_comentador
            ?.takeIf { it.isNotBlank() && it != "false" }
            ?.let {
                try {
                    val bytes = Base64.decode(it, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
                } catch (e: Exception) { null }
            }
    }

    val clickableModifier: Modifier = if (onPerfilClick != null && comentario.comentador_partner_id != null)
        Modifier.clickable {
            NavigationCache.profileImage = comentario.imagen_comentador
            onPerfilClick(comentario.comentador_partner_id, comentario.comentador)
        }
    else Modifier

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {

            // Cabecera
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    // Avatar + estrellas
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (avatarBitmap != null) {
                            Image(
                                bitmap = avatarBitmap,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .then(clickableModifier)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE8EEF4))
                                    .then(clickableModifier),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color(0xFF9E9E9E),
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }

                        if (comentario.valoracion != null) {
                            Spacer(Modifier.height(5.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                                (1..5).forEach { star ->
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        modifier = Modifier.size(13.dp),
                                        tint = if (star <= comentario.valoracion) Color(0xFFFFB800)
                                        else Color(0xFFDDDDDD)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.width(12.dp))

                    // Nombre + fecha
                    Column {
                        Text(
                            text = comentario.comentador.replaceFirstChar { it.uppercase() },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A2E),
                            modifier = clickableModifier
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = comentario.fecha_creacion.take(10),
                            fontSize = 12.sp,
                            color = Color(0xFF9E9E9E)
                        )
                    }
                }

                // Botones de acción
                Row {
                    if (esMio) {
                        IconButton(
                            onClick = { onEdit?.invoke() },
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar reseña",
                                modifier = Modifier.size(18.dp),
                                tint = Color(0xFF007EA7)
                            )
                        }
                        IconButton(
                            onClick = { onDelete?.invoke() },
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar reseña",
                                modifier = Modifier.size(18.dp),
                                tint = Color(0xFFE63946)
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { onReport?.invoke() },
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Flag,
                                contentDescription = "Denunciar",
                                modifier = Modifier.size(18.dp),
                                tint = Color(0xFFBDBDBD)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = comentario.contenido,
                fontSize = 15.sp,
                color = Color(0xFF333333),
                lineHeight = 23.sp
            )
        }
    }
}
