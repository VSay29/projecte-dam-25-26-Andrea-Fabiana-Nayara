package com.example.android_loop.view.vistas

import android.content.Context.MODE_PRIVATE
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.android_loop.utils.encriptarPasswd
import com.example.android_loop.utils.getToken
import com.example.android_loop.view.theme.Android_LoopTheme
import com.example.android_loop.view.theme.isDarkTheme
import com.example.android_loop.viewModel.AjustesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ajustes(navController: NavHostController, idioma: String) {

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("loop_prefs", MODE_PRIVATE)
    val token = getToken(context)

    val viewModelSettings: AjustesViewModel = viewModel()

    var dialogTipo by remember { mutableStateOf<String?>(null) }
    var email by remember { mutableStateOf("") }
    var passwd by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var tel by remember { mutableStateOf("") }
    var idioma by remember { mutableStateOf(idioma) }

    var expanded by remember { mutableStateOf(false) }
    val idiomas = listOf("Español", "Catálan", "Inglés")

    var mostrarDialogConfirmacion by remember { mutableStateOf(false) }
    var textoConfirmacion by remember { mutableStateOf("") }
    var inputConfirmacion by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

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

                        SettingItem("Cambiar idioma: $idioma") {
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
                                        viewModelSettings.cambiarIdioma(token!!, idioma)
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
                        viewModelSettings.cambiarCorreo(token, email)
                        dialogTipo = null
                    },
                    onDismiss = { dialogTipo = null },
                    confirmEnabled = true,
                    accionPeligrosa = false,
                    keyboardType = KeyboardType.Email
                )
            }

            "passwd" -> {
                MostrarDialog(
                    "Cambiar contraseña",
                    "Introduce la nueva contraseña",
                    value = passwd,
                    onValueChange = { passwd = it },
                    onConfirm = {
                        viewModelSettings.cambiarPasswd(token, encriptarPasswd(passwd))
                        dialogTipo = null
                    },
                    onDismiss = { dialogTipo = null },
                    confirmEnabled = true,
                    accionPeligrosa = false,
                    keyboardType = KeyboardType.Password
                )
            }

            "mobile" -> {
                MostrarDialog(
                    "Cambiar el número de contacto",
                    "Introduce el nuevo contacto",
                    value = mobile,
                    onValueChange = { mobile = it },
                    onConfirm = {
                        viewModelSettings.cambiarMobile(token!!, mobile)
                        dialogTipo = null
                    },
                    onDismiss = { dialogTipo = null },
                    confirmEnabled = true,
                    accionPeligrosa = false,
                    keyboardType = KeyboardType.Phone
                )
            }

            "telephone" -> {
                MostrarDialog(
                    "Cambiar telefono",
                    "Introduce el nuevo telefono",
                    value = tel,
                    onValueChange = { tel = it },
                    onConfirm = {
                        viewModelSettings.cambiarTelephone(token!!, tel)
                        dialogTipo = null
                    },
                    onDismiss = { dialogTipo = null },
                    confirmEnabled = true,
                    accionPeligrosa = false,
                    keyboardType = KeyboardType.Phone
                )
            }

            "cerrarSesion" -> {
                MostrarDialog(
                    "Cerrar sesión",
                    "Está a punto de cerrar sesión",
                    onConfirm = {
                        prefs.edit { putString("token", "") }
                        navController.navigate("login")
                        dialogTipo = null
                        Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                    },
                    onDismiss = {
                        dialogTipo = null
                    },
                    confirmEnabled = true,
                    accionPeligrosa = true,
                    value = null,
                    onValueChange = null,
                    keyboardType = null
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

                    scope.launch {

                        delay(1000)

                    }

                    navController.navigate("login")
                    viewModelSettings.borrarCuenta(token!!)
                    prefs.edit { putString("token", "") }
                    Toast.makeText(context, "La cuenta ha sido eliminada", Toast.LENGTH_SHORT).show()
                },
                onDismiss = {},
                confirmEnabled = (inputConfirmacion == textoConfirmacion),
                accionPeligrosa = true,
                null
            )
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
fun MostrarDialog(title: String, header: String, value: String?, onValueChange: ((String) -> Unit)?, onConfirm: () -> Unit, onDismiss: () -> Unit, confirmEnabled: Boolean, accionPeligrosa: Boolean, keyboardType: KeyboardType?) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(title) },
        text = {
            Column {
                Text(header)

                if(value != null && onValueChange != null) {

                    keyboardType?.let { it ->
                        OutlinedTextField(
                            value = value,
                            onValueChange = { onValueChange(it) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = it)
                        )
                    }

                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = confirmEnabled
            ) { Text(if(accionPeligrosa) "Confirmar" else "Guardar") }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) { Text("Cancelar") }
        }
    )
}

/**
 * Función para generar texto aleatorio y confirmar el borrado de cuenta
 * Devuelve un string con ese texto
 */

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
        Ajustes(navController = rememberNavController(), "Español")
    }
}
