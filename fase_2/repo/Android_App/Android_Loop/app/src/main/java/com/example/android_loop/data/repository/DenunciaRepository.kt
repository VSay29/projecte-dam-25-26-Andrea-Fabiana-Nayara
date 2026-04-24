package com.example.android_loop.data.repository

import com.example.android_loop.data.net.HttpClientProvider
import io.ktor.client.HttpClient

class DenunciaRepository(private val cliente: HttpClient = HttpClientProvider.cliente) {
}