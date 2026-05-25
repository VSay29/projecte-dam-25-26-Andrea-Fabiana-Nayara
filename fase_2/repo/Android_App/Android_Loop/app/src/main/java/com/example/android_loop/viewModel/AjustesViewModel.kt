package com.example.android_loop.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_loop.data.model_dataClass.usuarioResult.GetUserDataResult
import com.example.android_loop.data.repository.UsuarioRepository
import kotlinx.coroutines.launch

class AjustesViewModel(private val repository: UsuarioRepository = UsuarioRepository()) : ViewModel() {

    var settingsState by mutableStateOf<SettingsUiState>(SettingsUiState.Idle)
    var userData by mutableStateOf<GetUserDataResult?>(null)
        private set

    fun cargarDatosUsuario(token: String) {
        viewModelScope.launch {
            repository.getUserData(token).onSuccess { userData = it }
        }
    }

    private fun ejecutarCambio(accion: suspend () -> Result<*>) {
        viewModelScope.launch {
            settingsState = SettingsUiState.Loading
            val result = accion()
            settingsState = result.fold(
                onSuccess = { SettingsUiState.Success(true) },
                onFailure = { SettingsUiState.Error(it.message ?: "Operación fallida") }
            )
        }
    }

    fun cambiarCorreo(token: String, correo: String) =
        ejecutarCambio { repository.cambiarCorreo(token, correo) }

    fun cambiarPasswd(token: String, passwd: String) =
        ejecutarCambio { repository.cambiarPasswd(token, passwd) }

    fun cambiarMobile(token: String, mobile: String) =
        ejecutarCambio { repository.cambiarMobile(token, mobile) }

    fun cambiarTelephone(token: String, tel: String) =
        ejecutarCambio { repository.cambiarTelephone(token, tel) }

    fun cambiarIdioma(token: String, idioma: String) =
        ejecutarCambio { repository.cambiarIdioma(token, idioma.substring(0, 2).lowercase()) }

    fun borrarCuenta(token: String) =
        ejecutarCambio {
            Log.d("LOG_AJUSTES", "BORRANDO CUENTA")
            repository.borrarCuenta(token)
        }

    fun editarPerfil(token: String, correo: String, passwdActual: String, passwdNueva: String, mobile: String, tel: String) {
        viewModelScope.launch {
            settingsState = SettingsUiState.Loading
            var errorMsg: String? = null

            if (passwdNueva.isNotBlank()) {
                val username = userData?.username ?: ""
                repository.generarSesion(username, passwdActual)
                    .onFailure { errorMsg = "Contraseña actual incorrecta" }
            }

            if (correo.isNotBlank() && errorMsg == null)
                repository.cambiarCorreo(token, correo).onFailure { errorMsg = it.message ?: "Operación fallida" }
            if (passwdNueva.isNotBlank() && errorMsg == null)
                repository.cambiarPasswd(token, passwdNueva).onFailure { errorMsg = it.message ?: "Operación fallida" }
            if (mobile.isNotBlank() && errorMsg == null)
                repository.cambiarMobile(token, mobile).onFailure { errorMsg = it.message ?: "Operación fallida" }
            if (tel.isNotBlank() && errorMsg == null)
                repository.cambiarTelephone(token, tel).onFailure { errorMsg = it.message ?: "Operación fallida" }

            settingsState = if (errorMsg != null) SettingsUiState.Error(errorMsg!!) else SettingsUiState.Success(true)
        }
    }
}

sealed class SettingsUiState {
    object Idle : SettingsUiState()
    object Loading : SettingsUiState()
    data class Success(val resp: Boolean) : SettingsUiState()
    data class Error(val message: String) : SettingsUiState()
}
