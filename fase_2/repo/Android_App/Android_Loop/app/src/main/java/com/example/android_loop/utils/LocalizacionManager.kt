package com.example.android_loop.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import java.util.Locale

// DOC: Guardar y obtener ubicacion en SharedPreferences del usuario loggeado

/**
 * Guardar ubicación en SharedPreferences
 * Se convierte a Long usando toBits() y almacenarlos
 * con putLong(), esto evita perder la precisión
 */
fun guardarUbiSP(context: Context, lat: Double, lon: Double) {
    val prefs = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)
    prefs.edit {
        putLong("LATITUD", lat.toBits())
        putLong("LONGITUD", lon.toBits())
        apply()
    }
}

fun traducirLatLngAUbicacion(context: Context, ubi: DoubleArray): String {
    val geocoder = Geocoder(context, Locale.getDefault())
    val resultado = ""
    return try {
        val lista = geocoder.getFromLocation(ubi[0], ubi[1], 1)
        if (!lista.isNullOrEmpty()) {
            resultado.plus(lista[0].locality + ", " +lista[0].adminArea + ", " +lista[0].countryName)
        } else "No encontrado"
    } catch (ex: Exception) {
        "Error: " + ex.message
    }
}

fun normalizarLocation(ubi: String): DoubleArray {
    val trozos = ubi.split(",")
    return doubleArrayOf(trozos[0].toDouble(), trozos[1].toDouble())
}

@Composable
fun MapaCompose(location: DoubleArray) {
    GoogleMap(
        modifier = Modifier.fillMaxSize().height(300.dp),
        cameraPositionState = CameraPositionState(
            CameraPosition.fromLatLngZoom(
                LatLng(location[0], location[1]),
                12f
            )
        ),
    ) {
        Marker(
            state = MarkerState(position = LatLng(location[0], location[1]))
        )
        Circle(
            center = LatLng(location[0], location[1]),
            radius = 200.0,
            fillColor = Color.Blue
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
fun mostrarMapa(showMap: Boolean, onDismiss: () -> Unit, locationState: DoubleArray?): Boolean {
    var showMap1 = showMap
    if (showMap1 && locationState != null) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss() }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
               MapaCompose(locationState)
            }
        }
    }
    return showMap1
}