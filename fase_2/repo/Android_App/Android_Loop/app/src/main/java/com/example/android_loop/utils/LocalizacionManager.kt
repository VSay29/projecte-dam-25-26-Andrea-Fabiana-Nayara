package com.example.android_loop.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import java.util.Locale

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

// DOC: val EJEMPLO = obtenerUbiSP(context)
// DOC: Log.d("DEBUG_UBI", "Lat: ${EJEMPLO[0]}, Lon: ${EJEMPLO[1]}")

/**
 *
 *
 *
 */

fun obtenerUbiSP(context: Context): DoubleArray {
    val prefs = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)
    val lat = Double.fromBits(prefs.getLong("LATITUD", 0.0.toBits()))
    val lon = Double.fromBits(prefs.getLong("LONGITUD", 0.0.toBits()))
    return doubleArrayOf(lat, lon)
}

/**
 * @method Función para calcular la diferencia de distancia en metros entre usuario y producto (o vendedor)
 * @param locationMapa: localización del producto/vendedor
 * @param locationUsuario: localización del propio usuario
 * @return float con el resultado de la operación
 */

fun calcularDistancia(locationMapa: DoubleArray, locationUsuario: DoubleArray): Float {
    val resultado = FloatArray(1)

    Location.distanceBetween(locationMapa[0], locationMapa[1], locationUsuario[0], locationUsuario[1], resultado)

    return resultado[0]
}

suspend fun traducirLatLngAUbicacion(context: Context, ubi: DoubleArray): String =
    kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        try {
            val url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=${ubi[0]}&lon=${ubi[1]}"
            val connection = java.net.URL(url).openConnection() as java.net.HttpURLConnection
            connection.setRequestProperty("User-Agent", "AndroidLoop/1.0")
            connection.connectTimeout = 10_000
            connection.readTimeout = 10_000
            val response = connection.inputStream.bufferedReader().readText()
            val json = org.json.JSONObject(response)
            val address = json.getJSONObject("address")
            val city = address.optString("city")
                .ifEmpty { address.optString("town") }
                .ifEmpty { address.optString("village") }
                .ifEmpty { address.optString("municipality") }
                .ifEmpty { address.optString("county") }
            val state = address.optString("state")
            val country = address.optString("country")
            listOf(city, state, country).filter { it.isNotBlank() }.distinct().joinToString(", ")
        } catch (_: Exception) {
            ""
        }
    }


/**
 *
 *
 *
 */

fun normalizarLocation(ubi: String): DoubleArray {
    val trozos = ubi.split(",")
    return doubleArrayOf(trozos[0].toDouble(), trozos[1].toDouble())
}


/**
 *
 *
 *
 */

@Composable
fun MapaCompose(location: DoubleArray, mostrarDistancia: Boolean, ubiUsuario: DoubleArray) {
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
            fillColor = Color.Blue.copy(alpha = 0.25f),
            strokeColor = Color.Blue,
            strokeWidth = 3f
        )

        if(mostrarDistancia) {
            Polyline(
                points = listOf(
                    LatLng(
                        ubiUsuario[0],
                        ubiUsuario[1]
                    ),
                    LatLng(
                        location[0],
                        location[1]
                    )
                ),
                color = Color.Red,
                width = 8f
            )
        }
    }
}


/**
 *
 *
 *
 */

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
fun mostrarMapa(showMap: Boolean, onDismiss: () -> Unit, locationState: DoubleArray, context: Context): Boolean {

    val locationUsuario = obtenerUbiSP(context)
    val distancia = calcularDistancia(locationState, locationUsuario)
    var mostrarDistancia by rememberSaveable { mutableStateOf(false) }

    Log.d("DEBUG_MAPA", "$locationState")

    val showMap1 = showMap
    if (showMap1) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss() }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
               MapaCompose(locationState, mostrarDistancia, locationUsuario)
               Button(
                   onClick = { mostrarDistancia = !mostrarDistancia }
               ) {
                   Text(
                       if (mostrarDistancia) "Ocultar distancia (%.2f km)".format(distancia / 1000)
                       else "Ver distancia"
                   )
               }
            }
        }
    }
    return showMap1
}