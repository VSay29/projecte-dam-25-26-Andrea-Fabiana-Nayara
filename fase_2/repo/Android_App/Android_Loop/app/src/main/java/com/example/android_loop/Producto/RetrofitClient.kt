package com.example.android_loop.Producto

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    //private const val BASE_URL = "http://10.0.2.2:8069/" // si usas emulador
    private const val BASE_URL = "http://localhost:8069/"
    //private const val BASE_URL = "http://192.168.190.1/ "
   private const val TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjcsInVzZXJuYW1lIjoiQW5hIiwiZXhwIjoxNzcxNzk5OTAyLCJpYXQiOjE3NzE3OTYzMDJ9.3bvRnFj7OvZ-2vMMpDzmSWVuh50o0eAFbuvHGY4jKU8"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $TOKEN")
                .build()
            chain.proceed(request)
        }
        .addInterceptor(logging)
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}