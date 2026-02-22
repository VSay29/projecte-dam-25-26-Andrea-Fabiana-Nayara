package com.example.android_loop.Producto

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("api/products")
    suspend fun getProducts(): Response<ProductResponse>

    @POST("api/productos")
    suspend fun createProduct(
        @Body request: JsonRpcRequest<CreateProductRequest>
    ): Response<RespuestaJSON<CreateProductResponse>>

    @POST("api/v1/loop/etiquetas")
    suspend fun createEtiqueta(
        @Body request: CreateEtiquetaRequest
    ): Response<CreateEtiquetaResponse>

    @GET("api/v1/loop/etiquetas")
    suspend fun getEtiquetas(): Response<List<Etiqueta>>
}