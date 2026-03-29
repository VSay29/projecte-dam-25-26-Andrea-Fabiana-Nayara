package com.example.android_loop.ui.ajustes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.android_loop.data.repository.UserRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: UserRepository = UserRepository()): ViewModel() {

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
        settingsState = SettingsUiState.Idle
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
        settingsState = SettingsUiState.Idle
    }

    fun cambiarFotoPerfil(token: String, foto: String) {
        viewModelScope.launch {
            settingsState = SettingsUiState.Loading

            val result = repository.cambiarFotoPerfil(token, foto)

            settingsState = result.fold(
                onSuccess = {
                    SettingsUiState.Success(true)
                },
                onFailure = {
                    SettingsUiState.Error(it.message ?: "No se pudo actualizar la foto de perfil")
                }
            )
        }
        settingsState = SettingsUiState.Idle
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
        settingsState = SettingsUiState.Idle
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
        settingsState = SettingsUiState.Idle
    }

    fun cambiarIdioma(token: String, idioma: String) {
        viewModelScope.launch {
            settingsState = SettingsUiState.Loading

            val result = repository.cambiarIdioma(token, idioma)

            settingsState = result.fold(
                onSuccess = {
                    SettingsUiState.Success(true)
                },
                onFailure = {
                    SettingsUiState.Error(it.message ?: "No se pudo cambiar el idioma")
                }
            )
        }
        settingsState = SettingsUiState.Idle
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
        settingsState = SettingsUiState.Idle
    }

}