package com.example.android_loop.view.vistas

import android.content.Context.MODE_PRIVATE
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.android_loop.utils.getToken
import com.example.android_loop.utils.navegacionConfig.ROUTES
import com.example.android_loop.view.componentes.Boton_Componente
import com.example.android_loop.view.componentes.Header_Componente
import com.example.android_loop.view.theme.Android_LoopTheme
import com.example.android_loop.view.theme.isDarkTheme
import com.example.android_loop.viewModel.AjustesViewModel
import com.example.android_loop.viewModel.SettingsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ajustes(navController: NavHostController) {

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)
    var idioma by remember { mutableStateOf(prefs.getString("IDIOMA", "Español") ?: "Español") }
    val token = getToken(context)

    val viewModelSettings: AjustesViewModel = viewModel()
    val state = viewModelSettings.settingsState
    val userData = viewModelSettings.userData

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var mostrarEditarPerfil by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var passwdActual by remember { mutableStateOf("") }
    var passwdNueva by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var tel by remember { mutableStateOf("") }

    var dialogTipo by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val idiomas = listOf("Español", "Catálan", "English")

    var mostrarDialogConfirmacion by remember { mutableStateOf(false) }
    var textoConfirmacion by remember { mutableStateOf("") }
    var inputConfirmacion by remember { mutableStateOf("") }

    val isLoading = state is SettingsUiState.Loading
    val keyboardController = LocalSoftwareKeyboardController.current

    // Cargar datos del usuario al entrar en la pantalla
    LaunchedEffect(Unit) { viewModelSettings.cargarDatosUsuario(token) }

    // Pre-rellenar campos al abrir el sheet con los datos actuales
    LaunchedEffect(mostrarEditarPerfil) {
        if (mostrarEditarPerfil && userData != null) {
            email = userData.email
            mobile = userData.mobile ?: ""
            tel = userData.phone ?: ""
            passwdActual = ""
            passwdNueva = ""
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header_Componente(titulo = "Ajustes", onBack = { navController.popBackStack() })

            // ── Contenido con scroll ──────────────────────────────────
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                // Cuenta
                SectionTitle("Cuenta")
                Card(shape = MaterialTheme.shapes.large) {
                    Column {
                        SettingItem("Editar perfil") { mostrarEditarPerfil = true }
                        HorizontalDivider()
                        Box(modifier = Modifier.fillMaxWidth()) {
                            SettingItem("Idioma: ${idioma.substring(0, 2).toUpperCase()}") {
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
                                            prefs.edit { putString("IDIOMA", idioma) }
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                        HorizontalDivider()
                        SettingItem(
                            text = "Cerrar sesión",
                            textColor = MaterialTheme.colorScheme.error
                        ) { dialogTipo = "cerrarSesion" }
                    }
                }

                // Preferencias
                SectionTitle("Preferencias")
                Card(shape = MaterialTheme.shapes.large) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Modo oscuro", style = MaterialTheme.typography.bodyLarge)
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = {
                                isDarkTheme = it
                                prefs.edit { putBoolean("dark_mode", it) }
                            }
                        )
                    }
                }

                // Espacio extra al final del scroll para que no quede pegado al botón
                Spacer(modifier = Modifier.height(8.dp))
            }

            // ── Botón fijo en la parte inferior ──────────────────────
            HorizontalDivider()
            TextButton(
                onClick = {
                    textoConfirmacion = generarTextoConfirmacion()
                    inputConfirmacion = ""
                    mostrarDialogConfirmacion = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Eliminar cuenta de Loop",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
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

    // ── Bottom sheet: Editar perfil ───────────────────────────────────
    if (mostrarEditarPerfil) {
        ModalBottomSheet(
            onDismissRequest = { if (!isLoading) mostrarEditarPerfil = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Editar perfil", style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = passwdNueva,
                    onValueChange = { passwdNueva = it },
                    label = { Text("Nueva contraseña") },
                    placeholder = { Text("Dejar vacío para no cambiar") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
                if (passwdNueva.isNotBlank()) {
                    OutlinedTextField(
                        value = passwdActual,
                        onValueChange = { passwdActual = it },
                        label = { Text("Contraseña actual") },
                        placeholder = { Text("Requerida para confirmar el cambio") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                OutlinedTextField(
                    value = mobile,
                    onValueChange = { mobile = it },
                    label = { Text("Número de contacto") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = tel,
                    onValueChange = { tel = it },
                    label = { Text("Teléfono") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Boton_Componente(
                    texto = "Guardar cambios",
                    onClick = {
                        keyboardController?.hide()
                        viewModelSettings.editarPerfil(token, email, passwdActual, passwdNueva, mobile, tel)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading && (
                        email.isNotBlank() ||
                        (passwdNueva.isNotBlank() && passwdActual.isNotBlank()) ||
                        mobile.isNotBlank() ||
                        tel.isNotBlank()
                    )
                )
            }
        }
    }

    // ── Dialogs ───────────────────────────────────────────────────────
    when (dialogTipo) {
        "cerrarSesion" -> {
            MostrarDialog(
                "Cerrar sesión",
                "Está a punto de cerrar sesión",
                onConfirm = {
                    prefs.edit { putString("token", "") }
                    dialogTipo = null
                    Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                    navController.navigate(ROUTES.LOGIN)
                },
                onDismiss = { dialogTipo = null },
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
            onDismiss = { mostrarDialogConfirmacion = false },
            confirmEnabled = (inputConfirmacion == textoConfirmacion),
            accionPeligrosa = true,
            KeyboardType.Text,
            isLoading = isLoading
        )
    }

    LaunchedEffect(state) {
        when (state) {
            is SettingsUiState.Success -> {
                if (mostrarEditarPerfil) {
                    mostrarEditarPerfil = false
                    passwdActual = ""
                    passwdNueva = ""
                    viewModelSettings.cargarDatosUsuario(token)
                }
                Toast.makeText(context, "Guardado correctamente", Toast.LENGTH_SHORT).show()
                dialogTipo = null
                mostrarDialogConfirmacion = false
            }
            is SettingsUiState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }
}

@Composable
fun SectionTitle(text: String, color: Color = Color.Unspecified) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = if (color != Color.Unspecified) color else MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
fun SettingItem(
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    backgroundColor: Color = Color.Transparent,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(text = text, color = textColor, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun MostrarDialog(
    title: String,
    header: String,
    value: String?,
    onValueChange: ((String) -> Unit)?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmEnabled: Boolean,
    accionPeligrosa: Boolean,
    keyboardType: KeyboardType? = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isLoading: Boolean
) {
    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = { if (!isLoading) Text(title) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
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
                                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                                visualTransformation = visualTransformation
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (!isLoading) {
                Button(onClick = onConfirm, enabled = confirmEnabled) {
                    Text(if (accionPeligrosa) "Confirmar" else "Guardar")
                }
            }
        },
        dismissButton = {
            if (!isLoading) {
                Button(onClick = onDismiss) { Text("Cancelar") }
            }
        }
    )
}

fun generarTextoConfirmacion(): String {
    val caracteres = (
            ('a'..'z') +
                    ('A'..'Z') +
                    (0..9) +
                    listOf('!', '@', '#', '$', '%', '&', '*', '+', '-', '_')
            )
    return (1..12).map { caracteres.random() }.joinToString("")
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    Android_LoopTheme {
        Ajustes(navController = rememberNavController())
    }
}
