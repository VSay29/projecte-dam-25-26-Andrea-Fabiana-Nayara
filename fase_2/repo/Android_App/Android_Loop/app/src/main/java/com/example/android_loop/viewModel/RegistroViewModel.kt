package com.example.android_loop.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_loop.data.repository.UsuarioRepository
import com.example.android_loop.utils.encriptarPasswd
import kotlinx.coroutines.launch

class RegistroViewModel(private val repository: UsuarioRepository = UsuarioRepository()): ViewModel() {

    var registroState by mutableStateOf<RegistroUiState>(RegistroUiState.Idle)

    fun registro(name: String, username: String, email: String, passwd: String) {
        viewModelScope.launch {

            registroState = RegistroUiState.Loading

            val result = repository.registro(name, username, email, encriptarPasswd(passwd))

            registroState = result.fold(
                onSuccess = { RegistroUiState.Success(true) },
                onFailure = { RegistroUiState.Error(it.message ?: "Error desconocido") }
            )

        }
    }

}

sealed class RegistroUiState {

    object Idle : RegistroUiState()

    object Loading : RegistroUiState()

    data class Success(val resp: Boolean): RegistroUiState()

    data class Error(val message: String): RegistroUiState()

}