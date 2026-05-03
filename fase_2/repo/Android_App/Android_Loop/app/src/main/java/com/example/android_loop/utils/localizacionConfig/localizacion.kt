package com.example.android_loop.utils.localizacionConfig
/*
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationRequest
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback

class localizacion(private val context: Context) {

    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var locationState = mutableStateOf(doubleArrayOf(0.0,0.0))

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission
    ) {
        isGranted: Boolean ->
        if (isGranted) getLastLocation()
        else Toast.makeText(context, "No hay permisos de acceso al sensor de ubicación", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                locationState.value = doubleArrayOf(it.latitude, it.longitude)
            }
        }
    }

    // DOC: Configuración ciclos de vida

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback, Looper.getMainLooper())
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // DOC: petición de permisos

    fun requestPermissions() {
        if(ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        )

            requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
    }

}*/