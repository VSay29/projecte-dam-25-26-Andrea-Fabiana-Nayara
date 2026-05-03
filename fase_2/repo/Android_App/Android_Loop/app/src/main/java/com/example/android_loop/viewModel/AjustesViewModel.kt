package com.example.android_loop.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_loop.data.repository.UsuarioRepository
import kotlinx.coroutines.launch

class AjustesViewModel(private val repository: UsuarioRepository = UsuarioRepository()): ViewModel() {

    var settingsState by mutableStateOf<SettingsUiState>(SettingsUiState.Idle)

    fun cambiarCorreo(token: String, correo: String) {
        viewModelScope.launch {
            settingsState = SettingsUiState.Loading

            val result = repository.cambiarCorreo(token, correo)

            settingsState = result.fold(
                onSuccess = {
                    SettingsUiState.Success(true)
                },
                onFailure = {
                    SettingsUiState.Error(it.message ?: "No se pudo actualizar el correo")
                }
            )
        }
    }

    fun cambiarPasswd(token: String, passwd: String) {
        viewModelScope.launch {
            settingsState = SettingsUiState.Loading

            val result = repository.cambiarPasswd(token, passwd)

            settingsState = result.fold(
                onSuccess = {
                    SettingsUiState.Success(true)
                },
                onFailure = {
                    SettingsUiState.Error(it.message ?: "No se pudo actualizar la contraseña")
                }
            )
        }
    }

    fun cambiarMobile(token: String, mobile: String) {
        viewModelScope.launch {
            settingsState = SettingsUiState.Loading

            val result = repository.cambiarMobile(token, mobile)

            settingsState = result.fold(
                onSuccess = {
                    SettingsUiState.Success(true)
                },
                onFailure = {
                    SettingsUiState.Error(it.message ?: "No se pudo actualizar el número de contacto")
                }
            )
        }
    }

    fun cambiarTelephone(token: String, tel: String) {
        viewModelScope.launch {
            settingsState = SettingsUiState.Loading

            val result = repository.cambiarTelephone(token, tel)

            settingsState = result.fold(
                onSuccess = {
                    SettingsUiState.Success(true)
                },
                onFailure = {
                    SettingsUiState.Error(it.message ?: "No se pudo actualizar el telefono")
                }
            )
        }
    }

    fun cambiarIdioma(token: String, idioma: String) {
        viewModelScope.launch {
            settingsState = SettingsUiState.Loading

            val result = repository.cambiarIdioma(token, idioma.substring(0, 2).toLowerCase())

            settingsState = result.fold(
                onSuccess = {
                    SettingsUiState.Success(true)
                },
                onFailure = {
                    SettingsUiState.Error(it.message ?: "No se pudo cambiar el idioma")
                }
            )
        }
    }

    fun borrarCuenta(token: String) {
        viewModelScope.launch {
            settingsState = SettingsUiState.Loading

            val result = repository.borrarCuenta(token)

            settingsState = result.fold(
                onSuccess = {
                    SettingsUiState.Success(true)
                },
                onFailure = {
                    SettingsUiState.Error(it.message ?: "No se pudo borrar la cuenta")
                }
            )
        }
    }

}

sealed class SettingsUiState {

    object Idle : SettingsUiState()

    object Loading: SettingsUiState()

    data class Success(val resp: Boolean): SettingsUiState()

    data class Error(val message: String): SettingsUiState()

}