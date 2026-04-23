package com.example.android_loop.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_loop.data.repository.UsuarioRepository
import com.example.android_loop.utils.encriptarPasswd
import kotlinx.coroutines.launch

class LoginViewModel (private val repository: UsuarioRepository = UsuarioRepository()) : ViewModel() {

    var loginState by mutableStateOf<LoginUiState>(LoginUiState.Idle)

    fun login(username : String, passwd : String) {
        viewModelScope.launch {
            loginState = LoginUiState.Loading

            val result = repository.generarSesion(username, encriptarPasswd(passwd))

            loginState = try {
                result.fold(
                    onSuccess = { token -> LoginUiState.Success(token) },
                    onFailure = { LoginUiState.Error(it.message ?: "Nombre de usuario o contraseña incorrectos") }
                )
            } catch (e: Exception) {
                LoginUiState.Error(e.message ?: "Error")
            }

        }
    }

}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val token: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}