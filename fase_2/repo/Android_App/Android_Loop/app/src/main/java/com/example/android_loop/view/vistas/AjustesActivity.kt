package com.example.android_loop.view.vistas

import android.content.Context.MODE_PRIVATE
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.android_loop.utils.encriptarPasswd
import com.example.android_loop.utils.getToken
import com.example.android_loop.utils.navegacionConfig.ROUTES
import com.example.android_loop.view.theme.Android_LoopTheme
import com.example.android_loop.view.theme.isDarkTheme
import com.example.android_loop.viewModel.AjustesViewModel
import com.example.android_loop.viewModel.SettingsUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ajustes(navController: NavHostController) {

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)
    var idioma by remember { mutableStateOf(prefs.getString("IDIOMA", "Español") ?: "Español") }
    val token = getToken(context)

    val viewModelSettings: AjustesViewModel = viewModel()
    val state = viewModelSettings.settingsState

    var dialogTipo by remember { mutableStateOf<String?>(null) }
    var email by remember { mutableStateOf("") }
    var passwd by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var tel by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    val idiomas = listOf("Español", "Catálan", "English")

    var mostrarDialogConfirmacion by remember { mutableStateOf(false) }
    var textoConfirmacion by remember { mutableStateOf("") }
    var inputConfirmacion by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    val isLoading = state is SettingsUiState.Loading
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text("Cuenta", style = MaterialTheme.typography.titleMedium)

            Card {
                Column {

                    SettingItem("Cambiar correo") {
                        dialogTipo = "correo"
                    }
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                    SettingItem("Cambiar contraseña") {
                        dialogTipo = "passwd"
                    }
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                    SettingItem("Cambiar número de contacto") {
                        dialogTipo = "mobile"
                    }
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                    SettingItem("Cambiar número de telefono") {
                        dialogTipo = "telephone"
                    }
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                    Box(modifier = Modifier.fillMaxWidth()) {

                        SettingItem("Cambiar idioma: ${idioma.substring(0, 2).toUpperCase()}") {
                            expanded = true
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            idiomas.forEach {
                                DropdownMenuItem(
                                    text = { Text(it) },
                                    onClick = {
                                        idioma = it
                                        viewModelSettings.cambiarIdioma(token, idioma)
                                        prefs.edit {
                                            putString("IDIOMA", idioma)
                                        }
                                        expanded = false
                                    }
                                )
                            }
                        }

                    }
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                    SettingItem("Cerrar sesión", textColor = MaterialTheme.colorScheme.error) {
                        dialogTipo = "cerrarSesion"
                    }

                }
            }

            Text("Preferencias", style = MaterialTheme.typography.titleMedium)

            Card {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Modo oscuro")
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = {
                            isDarkTheme = it
                            prefs.edit { putBoolean("dark_mode", it) }
                        }
                    )
                }
            }

            Text("Acciones peligrosas", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.error)

            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SettingItem(text = "Borrar cuenta", textColor = MaterialTheme.colorScheme.error, backgroundColor = MaterialTheme.colorScheme.errorContainer
                    ) {

                        textoConfirmacion = generarTextoConfirmacion()

                        inputConfirmacion = ""

                        mostrarDialogConfirmacion = true

                    }
                }
            }
        }

        when (dialogTipo) {
            "correo" -> {
                MostrarDialog(
                    "Cambiar correo",
                    "Introduce el nuevo correo",
                    value = email,
                    onValueChange = { email = it },
                    onConfirm = {
                        keyboardController?.hide()
                        viewModelSettings.cambiarCorreo(token, email)
                    },
                    onDismiss = { if (!isLoading) dialogTipo = null },
                    confirmEnabled = true,
                    accionPeligrosa = false,
                    keyboardType = KeyboardType.Email,
                    isLoading = isLoading
                )
            }

            "passwd" -> {
                MostrarDialog(
                    "Cambiar contraseña",
                    "Introduce la nueva contraseña",
                    value = passwd,
                    onValueChange = { passwd = it },
                    onConfirm = {
                        keyboardController?.hide()
                        viewModelSettings.cambiarPasswd(token, encriptarPasswd(passwd))
                    },
                    onDismiss = { if (!isLoading) dialogTipo = null },
                    confirmEnabled = true,
                    accionPeligrosa = false,
                    keyboardType = KeyboardType.Password,
                    visualTransformation = PasswordVisualTransformation(),
                    isLoading = isLoading
                )
            }

            "mobile" -> {
                MostrarDialog(
                    "Cambiar el número de contacto",
                    "Introduce el nuevo contacto",
                    value = mobile,
                    onValueChange = { mobile = it },
                    onConfirm = {
                        keyboardController?.hide()
                        viewModelSettings.cambiarMobile(token, mobile)
                    },
                    onDismiss = { if (!isLoading) dialogTipo = null },
                    confirmEnabled = true,
                    accionPeligrosa = false,
                    keyboardType = KeyboardType.Phone,
                    isLoading = isLoading
                )
            }

            "telephone" -> {
                MostrarDialog(
                    "Cambiar telefono",
                    "Introduce el nuevo telefono",
                    value = tel,
                    onValueChange = { tel = it },
                    onConfirm = {
                        keyboardController?.hide()
                        viewModelSettings.cambiarTelephone(token, tel)
                    },
                    onDismiss = { if (!isLoading) dialogTipo = null },
                    confirmEnabled = true,
                    accionPeligrosa = false,
                    keyboardType = KeyboardType.Phone,
                    isLoading = isLoading
                )
            }

            "cerrarSesion" -> {
                MostrarDialog(
                    "Cerrar sesión",
                    "Está a punto de cerrar sesión",
                    onConfirm = {
                        prefs.edit { putString("token", "") }
                        dialogTipo = null

                        scope.launch {

                            delay(1000)

                        }

                        Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                        navController.navigate(ROUTES.LOGIN)
                    },
                    onDismiss = {
                        dialogTipo = null
                    },
                    confirmEnabled = true,
                    accionPeligrosa = true,
                    value = null,
                    onValueChange = null,
                    keyboardType = null,
                    isLoading = isLoading
                )
            }

        }

        if (mostrarDialogConfirmacion) {
            MostrarDialog(
                "¡Atención!",
                "La cuenta está a punto de ser eliminada.\n" +
                "Introduce el siguiente texto para confirmar esta acción:\n\n$textoConfirmacion",
                inputConfirmacion,
                { inputConfirmacion = it },
                onConfirm = {
                    viewModelSettings.borrarCuenta(token)
                    prefs.edit { putString("token", "") }
                    Toast.makeText(context, "La cuenta ha sido eliminada", Toast.LENGTH_SHORT).show()
                    navController.navigate(ROUTES.LOGIN)
                },
                onDismiss = {},
                confirmEnabled = (inputConfirmacion == textoConfirmacion),
                accionPeligrosa = true,
                KeyboardType.Text,
                isLoading = isLoading
            )
        }

        LaunchedEffect(state) {
            when (state) {
                is SettingsUiState.Loading -> {
                    Toast.makeText(context, "Cargando...", Toast.LENGTH_SHORT).show()
                }
                is SettingsUiState.Success -> {
                    Toast.makeText(context, "Guardado correctamente", Toast.LENGTH_SHORT).show()
                    dialogTipo = null
                    mostrarDialogConfirmacion = false
                }
                is SettingsUiState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }

            if (state is SettingsUiState.Success) {
                dialogTipo = null
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable(enabled = false) {},
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.size(16.dp))
                        Text("Cargando...", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

    }
}

@Composable
fun SettingItem(text: String, textColor: Color = MaterialTheme.colorScheme.onSurface, backgroundColor: Color = Color.Transparent, onClick: () -> Unit) {

    Row(Modifier.fillMaxWidth().background(backgroundColor).clickable{onClick()}.padding(16.dp)) {

        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.bodyLarge
        )

    }
}

@Composable
fun MostrarDialog(title: String, header: String, value: String?, onValueChange: ((String) -> Unit)?, onConfirm: () -> Unit, onDismiss: () -> Unit, confirmEnabled: Boolean, accionPeligrosa: Boolean, keyboardType: KeyboardType? = KeyboardType.Text, visualTransformation: VisualTransformation = VisualTransformation.None, isLoading: Boolean) {
    AlertDialog(
        onDismissRequest = {
            if (!isLoading) onDismiss()
        },
        title = {
            if (!isLoading) Text(title)
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(16.dp))
                        Text("Procesando...", style = MaterialTheme.typography.bodyMedium)
                    }
                } else {
                    Column {
                        Text(header)
                        Spacer(Modifier.height(8.dp))
                        if (value != null && onValueChange != null && keyboardType != null) {
                            OutlinedTextField(
                                value = value,
                                onValueChange = { onValueChange(it) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = keyboardType
                                ),
                                visualTransformation = visualTransformation
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (!isLoading) {
                Button(
                    onClick = onConfirm,
                    enabled = confirmEnabled
                ) {
                    Text(if (accionPeligrosa) "Confirmar" else "Guardar")
                }
            }
        },
        dismissButton = {
            if (!isLoading) {
                Button(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        }
    )
}

fun generarTextoConfirmacion() : String {
    val caracteres = (
        ('a' .. 'z') +
        ('A' .. 'Z') +
        (0 .. 9) +
        listOf('!', '@', '#', '$', '%', '&', '*', '+', '-', '_')
    )

    val longitud = 12

    val randomString = (1..longitud)
        .map { caracteres.random() }
        .joinToString("")

    return randomString
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    Android_LoopTheme {
        Ajustes(navController = rememberNavController())
    }
}
