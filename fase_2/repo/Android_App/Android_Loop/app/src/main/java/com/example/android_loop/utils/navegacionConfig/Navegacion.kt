package com.example.android_loop.utils.navegacionConfig

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
//import com.example.android_loop.data.Producto.CrearProducto
//import com.example.android_loop.data.Producto.Home
import com.example.android_loop.utils.getToken
import com.example.android_loop.utils.tokenValido
import com.example.android_loop.view.componentes.TabMenu
//import com.example.android_loop.view.ui.Producto.VerProducto
import com.example.android_loop.view.vistas.Ajustes
import com.example.android_loop.view.vistas.CrearProducto
import com.example.android_loop.view.vistas.Home
//import com.example.android_loop.view.ui.compra.Compra
//import com.example.android_loop.view.ui.favoritos.Favoritos
import com.example.android_loop.view.vistas.Loggeo
import com.example.android_loop.view.vistas.PerfilUsuario
//import com.example.android_loop.view.ui.perfilUsuario.PerfilUsuario
import com.example.android_loop.view.vistas.PerfilVendedor
import com.example.android_loop.view.vistas.Registro
import com.example.android_loop.view.vistas.VerProducto

//import com.example.android_loop.view.ui.shoppingCart.Carrito

@Composable
fun AppNavGraph(navController: NavHostController) {

    val context = LocalContext.current

    val rutaInicio: String = if (tokenValido(getToken(context))) ROUTES.HOME
    else ROUTES.LOGIN

    val rutasSinMenu = listOf(
        ROUTES.LOGIN,
        ROUTES.REGISTRO,
        ROUTES.CREAR_PRODUCTO,
        ROUTES.PERFIL_VENDEDOR
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (!rutasSinMenu.contains(currentRoute)) {
                TabMenu(navController = navController)
            }
        }
    ) { paddingValues ->

        NavHost(
        navController = navController,
        startDestination = rutaInicio,
        modifier = Modifier.Companion.padding(paddingValues)
    ) {
            composable(ROUTES.LOGIN) { Loggeo(navController) }
            composable(ROUTES.REGISTRO) { Registro(navController) }
            composable(ROUTES.PERFIL_USUARIO) { PerfilUsuario(navController) }
            //composable(ROUTES.FAVORITOS) { Favoritos(navController) }
            //composable(ROUTES.COMPRA) { Compra(navController) }
            //composable(ROUTES.CARRITO) { Carrito(navController) }
            composable(ROUTES.CREAR_PRODUCTO) { CrearProducto(navController) }
            composable(ROUTES.HOME) { Home(navController) }

            // Rutas parametrizadas

            composable(ROUTES.VER_PRODUCTO_ROUTE, listOf(navArgument("productId") { type = NavType.IntType })) {
                backStackEntry -> val productId = backStackEntry.arguments!!.getInt("productId")
                VerProducto(productId, navController)
            }

            // TODO: EN AJUSTES SE PASA POR PARAM EL IDIOMA, PERO LO IDEAL
            // TODO: SERÍA QUE SE GUARDARA EN SHAREDPREFERENCES, PARA QUE
            // TODO: EN EL FUTURO SE PUEDA INTEGRAR CAMBIO DE IDIOMA REAL

            composable(ROUTES.AJUSTES, listOf(navArgument("idioma") { type = NavType.StringType })) {
                backStackEntry -> val idioma = backStackEntry.arguments!!.getString("idioma") ?: "Español"
                Ajustes(navController, idioma)
            }

            composable(ROUTES.PERFIL_VENDEDOR,
                listOf(
                    navArgument("vendedorId") { type = NavType.IntType },
                    navArgument("vendedorNombre") { type = NavType.StringType }
                )
            ) {
                backStackEntry ->
                val vendedorId = backStackEntry.arguments!!.getInt("vendedorId")
                val vendedorNombre = backStackEntry.arguments!!.getString("vendedorNombre")
                PerfilVendedor(vendedorId, vendedorNombre!!, navController)
            }

        }

    }

}

