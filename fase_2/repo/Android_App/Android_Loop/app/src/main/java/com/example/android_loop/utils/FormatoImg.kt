package com.example.android_loop.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Base64.decode
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

fun toBase64(context: Context, uri: Uri): String {
    val inputStream = context.contentResolver.openInputStream(uri)
    val original = BitmapFactory.decodeStream(inputStream) ?: return ""

    val maxSize = 1024
    val ratio = minOf(maxSize.toFloat() / original.width, maxSize.toFloat() / original.height)
    val scaled = if (ratio < 1f)
        Bitmap.createScaledBitmap(original, (original.width * ratio).toInt(), (original.height * ratio).toInt(), true)
    else original

    val out = ByteArrayOutputStream()
    scaled.compress(Bitmap.CompressFormat.JPEG, 75, out)
    return Base64.encodeToString(out.toByteArray(), Base64.DEFAULT)
}

fun base64ToImage(base64: String): ImageBitmap {
    val decodedString = decode(base64, Base64.DEFAULT)
    val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    return bitmap.asImageBitmap()
}