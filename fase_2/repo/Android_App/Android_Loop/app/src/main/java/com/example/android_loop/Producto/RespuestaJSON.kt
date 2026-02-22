package com.example.android_loop.Producto

data class RespuestaJSON<T>(
    val jsonrpc: String,
    val id: String?,
    val result: T?
)

data class JsonRpcRequest<T>(
    val jsonrpc: String = "2.0",
    val method: String = "call",
    val params: T
)