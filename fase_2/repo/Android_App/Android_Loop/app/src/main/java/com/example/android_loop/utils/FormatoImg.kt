package com.example.android_loop.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Base64.decode
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun toBase64(context: Context, uri: Uri): String {
    val inputStream = context.contentResolver.openInputStream(uri)
    val bytes = inputStream?.readBytes()
    return Base64.encodeToString(bytes, Base64.DEFAULT)
}

fun base64ToImage(base64: String): ImageBitmap {
    val decodedString = decode(base64, Base64.DEFAULT)
    val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    return bitmap.asImageBitmap()
}