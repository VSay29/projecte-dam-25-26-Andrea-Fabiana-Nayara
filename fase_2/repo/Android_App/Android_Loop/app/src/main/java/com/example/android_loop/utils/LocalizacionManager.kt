package com.example.android_loop.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit

// DOC: Guardar y obtener ubicacion en SharedPreferences

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

/**
 * Obtener ubicacion desde SharedPreferences
 * Devuelve latitud y longitud en un DoubleArray
 */


// DOC: EJEMPLO DE COMO SACAR LA UBI CON ESTA FUNCION:
// val EJEMPLO = obtenerUbiSP(context)
// Log.d("DEBUG_UBI", "Lat: ${EJEMPLO[0]}, Lon: ${EJEMPLO[1]}")

fun obtenerUbiSP(context: Context): DoubleArray {
    val prefs = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)
    val lat = Double.fromBits(prefs.getLong("LATITUD", 0.0.toBits()))
    val lon = Double.fromBits(prefs.getLong("LONGITUD", 0.0.toBits()))
    return doubleArrayOf(lat, lon)
}