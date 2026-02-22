package com.example.android_loop.Producto

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {

    @GET("api/products")
    suspend fun getProducts(
        @Header("Authorization") token: String
    ): Response<ProductResponse>
}