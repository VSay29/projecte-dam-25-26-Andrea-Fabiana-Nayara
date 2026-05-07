package com.example.android_loop.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.android_loop.utils.guardarUbiSP
import com.example.android_loop.utils.navegacionConfig.AppNavGraph
import com.example.android_loop.view.theme.Android_LoopTheme
import com.example.android_loop.view.theme.isDarkTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class MainActivity : ComponentActivity() {

    // SECCION: VARIABLES

    private lateinit var fusedLocationClient : FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var locationState = mutableStateOf(doubleArrayOf(0.0, 0.0))

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) getLastLocation()
        else println("No hay permisos de acceso al sensor de ubicación.")
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DOC: Configuración de la ubicación

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        requestPermissions()

        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            60 * 60 * 1000
        ).build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.lastOrNull()?.let { location ->
                    locationState.value = doubleArrayOf(
                        location.latitude,
                        location.longitude
                    )
                    guardarUbiSP(this@MainActivity, locationState.value[0], locationState.value[1])
                }
            }
        }

        // DOC: Configuración modo oscuro

        val prefs = getSharedPreferences("loop_prefs", MODE_PRIVATE)
        isDarkTheme = prefs.getBoolean("dark_mode", false)


        enableEdgeToEdge()

        // DOC: SETCONTENT -- Navegación
        // DOC: AppNavGraph: Navegará a Home o a Login al iniciar la app dependiendo de la válidez del token

        setContent {                                         
            Android_LoopTheme {
                val navController = rememberNavController()
                AppNavGraph(navController)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                locationState.value = doubleArrayOf(it.latitude, it.longitude)
                guardarUbiSP(this, it.latitude, it.longitude)
            }
        }
    }

    // SECCION: Configuración ciclos de vida

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

    // SECCION: Petición de permisos

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

}