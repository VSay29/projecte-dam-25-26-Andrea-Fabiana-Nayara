package com.example.android_loop.data.repository

import com.example.android_loop.data.net.HttpClientProvider
import com.example.android_loop.data.net.Servidor
import com.example.android_loop.data.model_dataClass.productoResult.ProductosResult
import com.example.android_loop.data.model_dataClass.categoriaResult.Categoria
import com.example.android_loop.data.model_dataClass.categoriaResult.CategoriasResponse
import com.example.android_loop.data.model_dataClass.productoResult.CreateProductRequest
import com.example.android_loop.data.model_dataClass.productoResult.CreateProductResponse
import com.example.android_loop.data.model_dataClass.productoResult.CreateProductRpcResponse
import com.example.android_loop.data.model_dataClass.productoResult.ImagenDetalle
import com.example.android_loop.data.model_dataClass.productoResult.ImagenesProductoResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.put

class ProductoRepository (private val cliente: HttpClient = HttpClientProvider.cliente) {

    // GET PRODUCTOS

    suspend fun getProductos(token: String): Result<ProductosResult> {
        return try {
            val response: ProductosResult =
                cliente.get("${Servidor.BASE_URL}/api/products") {
                    header("Authorization", "Bearer $token")
                    accept(ContentType.Application.Json)
                }.body()

            Result.success(response)

        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    // CREATE PRODUCTOS

    suspend fun createProduct(
        token: String,
        request: CreateProductRequest
    ): Result<CreateProductResponse> {

        return try {

            val response: CreateProductRpcResponse =
                cliente.post("${Servidor.BASE_URL}/api/productos") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(buildJsonObject {
                        put("jsonrpc", "2.0")
                        put("method", "call")
                        put("params", Json.encodeToJsonElement(request))
                    })
                }.body()

            val result = response.result
                ?: throw Exception("Error Odoo: ${response.error?.toString() ?: "sin detalles"}")
            Result.success(result)

        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    /* TODO: CREAR RESPUESTA PARA OBTENER LAS IMAGENES, YA QUE ESTA NOS PUEDE SERVIR PARA ABRIR LA GALERIA QUE
       TODO: PODREMOS ABRIR DE DETALLE PRODUCTO PARA VER MEJOR LAS IMAGENES

    // GET PRODUCTOS IMAGES

    */

    suspend fun getProductImages(token: String, productId: Int): Result<List<ImagenDetalle>> {
        return try {

            val response: ImagenesProductoResponse = cliente.get("${Servidor.BASE_URL}/api/v1/loop/productos/$productId/imagenes") {
                    header("Authorization", "Bearer $token")
                    accept(ContentType.Application.Json)
                }.body()

            Result.success(response.imagenes)

        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }



    // GET CATEGORIAS PRODUCTOS

    suspend fun getCategoriasProductos(token: String): Result<List<Categoria>> {
        return try {
            val response: CategoriasResponse =
                cliente.get("${Servidor.BASE_URL}/api/v1/loop/categorias") {
                    header("Authorization", "Bearer $token")
                    accept(ContentType.Application.Json)
                }.body()
            Result.success(response.categorias)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

}