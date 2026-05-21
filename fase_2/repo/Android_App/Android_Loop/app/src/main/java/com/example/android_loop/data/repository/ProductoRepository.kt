package com.example.android_loop.data.repository

import com.example.android_loop.data.net.HttpClientProvider
import com.example.android_loop.data.net.Servidor
import com.example.android_loop.data.model_dataClass.productoResult.ProductosResult
import com.example.android_loop.data.model_dataClass.categoriaResult.Categoria
import com.example.android_loop.data.model_dataClass.categoriaResult.CategoriasResponse
import com.example.android_loop.data.model_dataClass.productoResult.CreateProductRequest
import com.example.android_loop.data.model_dataClass.productoResult.CreateProductResponse
import com.example.android_loop.data.model_dataClass.productoResult.CreateProductRpcResponse
import com.example.android_loop.data.model_dataClass.productoResult.DeleteProductResponse
import com.example.android_loop.data.model_dataClass.productoResult.ImagenDetalle
import com.example.android_loop.data.model_dataClass.productoResult.ImagenesProductoResponse
import com.example.android_loop.data.model_dataClass.productoResult.UpdateProductRequest
import com.example.android_loop.data.model_dataClass.productoResult.UpdateProductResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
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

    suspend fun createProduct(token: String, request: CreateProductRequest): Result<CreateProductResponse> {

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


    // MODIFICAR PRODUCTO

    suspend fun modificarProducto(token: String, productoId: Int, request: UpdateProductRequest): Result<UpdateProductResponse> {

        return try {

            val response: UpdateProductResponse = cliente.put("${Servidor.BASE_URL}/api/products/$productoId") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()

            Result.success(response)
        } catch (ex: Exception) {
            Result.failure(ex)
        }

    }


    // ELIMINAR PRODUCTO

    suspend fun eliminarProducto(token: String, productoId: Int): Result<Boolean> {

        return try {

            val response: DeleteProductResponse = cliente.delete("${Servidor.BASE_URL}/api/products/$productoId") {
                header("Authorization", "Bearer $token")
                accept(ContentType.Application.Json)
            }.body()

            val result = response.ok
            Result.success(result)
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