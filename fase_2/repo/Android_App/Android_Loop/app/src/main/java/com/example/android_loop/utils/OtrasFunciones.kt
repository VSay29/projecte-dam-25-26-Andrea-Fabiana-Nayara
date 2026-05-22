package com.example.android_loop.utils

import android.content.Context
import android.net.Uri
import android.util.Base64
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.android_loop.data.model_dataClass.productoResult.ImageRequest
import com.example.android_loop.data.model_dataClass.productoResult.ImagenDetalle
import java.io.File
import java.io.FileOutputStream
import java.text.Normalizer
import java.util.UUID

fun String.sinAcentos(): String {
    val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
    return normalized.replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
}

fun convertirListImgToListB64(imageUris: SnapshotStateList<Uri>?, context: Context): List<ImageRequest> {

    val imageRequest = mutableListOf<ImageRequest>()

    imageUris?.forEachIndexed { index, uri ->

        val base64Image = toBase64(context, uri)

        imageRequest.add(
            ImageRequest(
                imagen = base64Image,
                is_principal = index == 0,
                sequence = index + 1
            )
        )

    }

    return imageRequest

}

fun convertirListB64ToUri(lista: List<ImagenDetalle>, context: Context): List<Uri> {
    return lista.mapNotNull { imgDetalle ->
        imgDetalle.imagen.b64ToUri(context)
    }
}

fun String.b64ToUri(context: Context): Uri? {
    return try {
        val b64SinPrefijo = this.substringAfter(",")
        val imageBytes = Base64.decode(b64SinPrefijo, Base64.DEFAULT)
        val archivoTemp = File.createTempFile("temp_img_${UUID.randomUUID()}", ".jpg", context.cacheDir)
        FileOutputStream(archivoTemp).use { fos ->
            fos.write(imageBytes)
        }
        Uri.fromFile(archivoTemp)
    } catch (_: Exception) {
        "Archivo corrupto"
    } as Uri?
}



