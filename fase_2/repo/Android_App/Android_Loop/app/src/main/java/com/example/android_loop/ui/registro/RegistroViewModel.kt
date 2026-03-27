package com.example.android_loop.ui.registro

import android.util.Log
import android.util.Log.println
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_loop.data.repository.UserRepository
import kotlinx.coroutines.launch

class RegistroViewModel(private val repository: UserRepository = UserRepository()): ViewModel() {

    var registroState by mutableStateOf<RegistroUiState>(RegistroUiState.Idle)

    fun registro(name: String, username: String, email: String, passwd: String) {
        viewModelScope.launch {
            registroState = RegistroUiState.Loading

            val result = repository.registro(name, username, email, passwd)

            registroState = result.fold(
                onSuccess = {
                    RegistroUiState.Success(true)
                },
                onFailure = {
                    RegistroUiState.Error(it.message ?: "Error desconocido")
                }
            )
        }
    }

}