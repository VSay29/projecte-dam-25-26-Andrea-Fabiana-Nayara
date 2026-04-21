package com.example.android_loop.view.vistas

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.tuapp.ui.theme.Primary
import com.tuapp.ui.theme.Secondary
import com.example.android_loop.view.componentes.PantallaHeader
import com.example.android_loop.view.componentes.BotonCrearProducto
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_loop.utils.getToken
import com.example.android_loop.utils.sinAcentos
import com.example.android_loop.viewModel.CrearProductoUiState
import com.example.android_loop.viewModel.CrearProductoViewModel
import com.tuapp.ui.theme.OnPrimary
import kotlin.collections.emptyList


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CrearProducto(navController: NavController) {

    val context = LocalContext.current
    val token = getToken(context)

    val scrollState = rememberScrollState()

    // SECCION: Declaración de los viewmodels

    val crearProductoVM: CrearProductoViewModel = viewModel()
    val crearProductoState = crearProductoVM.crearProductoUiState
    val etiquetasState = crearProductoVM.cargarEtiquetaUiState
    val categoriasState = crearProductoVM.cargarCategoriasUiState

    // SECCION: VARIABLES PARA LA REQUEST DE CREAR PRODUCTO

    var nombre by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var precio by rememberSaveable { mutableStateOf("0") }
    var ubicacion by rememberSaveable { mutableStateOf("39.324, -125.525") }
    val selectedEtiquetas = rememberSaveable { mutableStateListOf<Int>() }
    var busquedaEtiqueta by remember { mutableStateOf("") }
    var mostrarSugerencias by remember { mutableStateOf(false) }
    val estados = listOf("nuevo", "segunda_mano", "reacondicionado")
    var estado by rememberSaveable { mutableStateOf(estados[0]) }
    var categoriaId by rememberSaveable { mutableIntStateOf(0) }
    val imageUris = remember { mutableStateListOf<Uri>() }


    // Formato para la fecha de antiguedad
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // Lista de etiquetas ya creadas y cargadas
    val etiquetasCargadas = when (etiquetasState) {
        is CrearProductoUiState.SuccessCargarEtiquetas -> etiquetasState.resp
        else -> emptyList()
    }

    // Lista de categorias ya cargadas
    val categoriasCargadas = when (categoriasState) {
        is CrearProductoUiState.SuccessCargarCategorias -> categoriasState.resp
        else -> emptyList()
    }

    // SECCION: ABRIR ADMINISTRADOR DE ARCHIVOS (GALERÍA EN ESTE CASO) Y OBTENER VARIAS IMAGENES (MÍN: 1, MÁX: 10)

    val launcherGaleria = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents()) { uris ->
        if (uris.isNotEmpty() && uris.size <= 10) imageUris.addAll(uris)
        else {
            // TODO: Abrir un dialogo para informar de que deben elegirse mínimo 1 imagen, y máximo 10
        }
    }

    // SECCION: CARGAR LAUNCHEDEFFECT

    LaunchedEffect(Unit) {
        crearProductoVM.cargarEtiquetas(token)
        crearProductoVM.cargarCategorias(token)
    }

    // TODO: AL CREAR UN PRODUCTO CON ÉXITO LIMPIAREMOS LOS CAMPOS Y MOSTRAREMOS UN DIALOGO

    LaunchedEffect(crearProductoState) {
        if (crearProductoState is CrearProductoUiState.SuccessCrearProducto) {
            // TODO: Mostrar dialogo aquí
            Toast.makeText(context, "Producto creado", Toast.LENGTH_SHORT).show()
            clearAllInputs()
        }
    }

    // Column exterior: ocupa toda la pantalla pero NO tiene scroll
    // El scroll solo está en el contenido, no en el botón
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // Header a tope con los bordes, fuera del scroll
        // TODO: Agregar un botón para volver a la pantalla ROUTES.HOME
        PantallaHeader(titulo = "Crear Producto")

        // Column interior: weight(1f) = ocupa el espacio disponible excepto el botón de abajo
        // El scroll está aquí para que solo el contenido se desplace, no el botón
        Column(modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        Spacer(modifier = Modifier.height(8.dp))

        // SECCION: Fila que contiene en un box todas las imagenes añadidas

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(imageUris){ uri ->

                Box(
                    modifier = Modifier.size(120.dp).clip(RoundedCornerShape(12.dp))
                ) {

                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                    )

                    // SECCION: Botón eliminar para quitar imagen

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(24.dp)
                            .background(
                                color = Color.Black.copy(alpha = 0.6f),
                                shape = CircleShape
                            )
                            .clickable {
                                imageUris.remove(uri)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Eliminar",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // SECCION: Botón para elegir las fotos

            item {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { launcherGaleria.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Text("Subir fotos")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))


        // SECCION: COLORES
        // Colores personalizados para todos los OutlinedTextField
        // Se define una sola vez y se reutiliza en cada campo para no repetir código
        val campoColores = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Secondary,
            unfocusedBorderColor = Primary,
            focusedLabelColor = Secondary,
            unfocusedLabelColor = Primary,
            cursorColor = Secondary,
            disabledBorderColor = Primary,
            disabledLabelColor = Primary
        )

        // SECCION: Campos a introducir

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            colors = campoColores,
            isError = (nombre.isEmpty())
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            colors = campoColores,
            isError = descripcion.isEmpty()
        )

        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth(),
            colors = campoColores,
            isError = precio.isEmpty() || (precio.toDoubleOrNull()?.let { it < 0 } ?: true),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        /* URGENTE: Vamos a quitar este campo y en su lugar se hará una configuración
           URGENTE: para que recoja los param de ubicación automáticamente (lon, lat)*/

        Spacer(modifier = Modifier.height(12.dp))

        var expandedEstado by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expandedEstado,
            onExpandedChange = { expandedEstado = !expandedEstado }
        ) {
            OutlinedTextField(
                value = estado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Estado") },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                colors = campoColores
            )

            ExposedDropdownMenu(
                expanded = expandedEstado,
                onDismissRequest = { expandedEstado = false }
            ) {
                estados.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            estado = it
                            expandedEstado = false
                        }
                    )
                }
            }
        }




            // SECCION: ETIQUETAS

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Etiquetas (máx. 5)",
                style = MaterialTheme.typography.titleMedium,
                color = Primary
            )

            Spacer(modifier = Modifier.height(8.dp))


            // FlowRow distribuye las etiquetas en filas automáticamente si no caben en una sola
            if (selectedEtiquetas.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    selectedEtiquetas.forEach { id ->
                        val etiqueta = etiquetasCargadas.find { it.id == id }
                        etiqueta?.let {
                            InputChip(
                                selected = true,
                                onClick = { selectedEtiquetas.remove(id) },
                                label = { Text("#${it.name}") },
                                trailingIcon = {
                                    // Icono X para quitar la etiqueta
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Quitar",
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                colors = InputChipDefaults.inputChipColors(
                                    selectedContainerColor = Secondary,
                                    selectedLabelColor = OnPrimary,
                                    selectedTrailingIconColor = OnPrimary
                                )
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // campo de búsqueda etiquetas
                OutlinedTextField(
                    value = busquedaEtiqueta,
                    onValueChange = {
                        busquedaEtiqueta = it
                        mostrarSugerencias = true // al escribir abre el desplegable
                    },
                    label = { Text("Buscar etiqueta...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            // Al hacer click en el campo también abre el desplegable
                            if (focusState.isFocused) mostrarSugerencias = true
                        },
                    colors = campoColores,
                    singleLine = true,
                    enabled = selectedEtiquetas.size < 5 // El campo sólo estará habilitado si no hay más de 5 etiquetas
                )

            // lista desplegable filtrada según lo que escribe el usuario
            val sugerencias = etiquetasCargadas.filter { etiqueta ->
                etiqueta.name.sinAcentos().contains(busquedaEtiqueta, ignoreCase = true) && !selectedEtiquetas.contains(etiqueta.id)
            }

            // Calculamos si el texto escrito no existe en la lista completa de etiquetas
            val textoNuevo = busquedaEtiqueta.trim()
            val noExiste = textoNuevo.isNotEmpty() && etiquetasCargadas.none { it.name.equals(textoNuevo, ignoreCase = true) }

            // El Card se muestra si hay sugerencias O si hay un texto nuevo para crear
            // Antes solo se mostraba con sugerencias, por eso desaparecía al escribir más letras
            if (mostrarSugerencias && (sugerencias.isNotEmpty() || noExiste)) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    sugerencias.forEach { etiqueta ->
                        Text(
                            text = "#${etiqueta.name}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedEtiquetas.add(etiqueta.id)
                                    busquedaEtiqueta = ""
                                    mostrarSugerencias = false
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            color = Primary
                        )
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                    }

                    if (noExiste) {
                        Text(
                            text = "+ Crear \"#$textoNuevo\"",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    crearProductoVM.crearEtiqueta(token, textoNuevo) { nuevoId ->
                                        selectedEtiquetas.add(nuevoId)
                                    }
                                    busquedaEtiqueta = ""
                                    mostrarSugerencias = false
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            color = Secondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))



            // SECCION: CATEGORIAS

            var expandedCategoria by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expandedCategoria,
                onExpandedChange = { expandedCategoria = !expandedCategoria }
            ) {
                OutlinedTextField(
                    value = categoriasCargadas.find { it.id == categoriaId }?.nombre ?: "Seleccione categoría",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    colors = campoColores
                )
                ExposedDropdownMenu(
                    expanded = expandedCategoria,
                    onDismissRequest = { expandedCategoria = false }
                ) {
                    categoriasCargadas.forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria.nombre) },
                            onClick = {
                                categoriaId = categoria.id
                                expandedCategoria = false
                            }
                        )
                    }
                }
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        BotonCrearProducto(
            texto = "Guardar Producto",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            onClick = {

                /* TODO: Para controlar que se crea el producto sin errores se hará lo siguiente:
                   TODO: Deshabilitar el botón de guardar botón hasta que los datos cumplan los requisitos
                */

                crearProductoVM.crearProducto(
                    token = token,
                    context = context,
                    nombre = nombre,
                    descripcion = descripcion,
                    precio = precio.toDoubleOrNull() ?: 0.0,
                    estado = estado,
                    ubicacion = ubicacion,
                    antiguedad = formatter.format(Date()),
                    categoriaId = categoriaId,
                    imageUris = imageUris
                )
            },
            enabled = todoCorrecto(nombre, descripcion, precio.toDoubleOrNull() ?: -1.0, estado, ubicacion, categoriaId, imageUris)
        )

    }
}

// SECCION: OTRAS FUNCIONES

// TODO: LIMPIAR TODOS LOS INPUTS AL CREAR PRODUCTO

private fun clearAllInputs() {



}

private fun todoCorrecto(nombre: String, desc: String, precio: Double, estado: String, ubicacion: String, categoria: Int, imageUris: List<Uri>): Boolean {

    val noVacio = (nombre.isNotEmpty() && desc.isNotEmpty() && precio.toString().isNotEmpty() && estado.isNotEmpty() && ubicacion.isNotBlank() && categoria.toString().isNotEmpty() && imageUris.isNotEmpty())
    val datosCorrectos = (precio >= 0) && (imageUris.size in 1..10)

    return (noVacio && datosCorrectos)

}