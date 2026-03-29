package com.example.android_loop.ui.ajustes

sealed class SettingsUiState {

    object Idle : SettingsUiState()

    object Loading: SettingsUiState()

    data class Success(val resp: Boolean): SettingsUiState()

    data class Error(val message: String): SettingsUiState()

}