package com.example.android_loop.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_loop.data.repository.UsuarioRepository
import kotlinx.coroutines.launch

class RegistroViewModel(private val repo: UsuarioRepository = UsuarioRepository()): ViewModel() {

    var registroState by mutableStateOf<RegistroUiState>(RegistroUiState.Idle)

    fun registro(name: String, username: String, email: String, passwd: String) {
        viewModelScope.launch {

            registroState = RegistroUiState.Loading

            repo.registro(name, username, email, passwd)

                .onSuccess { result ->

                if (result.error.isNotEmpty()) registroState = RegistroUiState.Error(result.error)
                else if (result.success) registroState = RegistroUiState.Success(true)

                }

                .onFailure { registroState = RegistroUiState.Error(it.message ?: "Error de conexión") }



        }
    }

}

sealed class RegistroUiState {

    object Idle : RegistroUiState()

    object Loading : RegistroUiState()

    data class Success(val resp: Boolean): RegistroUiState()

    data class Error(val message: String): RegistroUiState()

}