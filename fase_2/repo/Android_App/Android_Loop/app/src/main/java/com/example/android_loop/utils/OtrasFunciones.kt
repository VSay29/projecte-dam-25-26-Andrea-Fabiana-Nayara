package com.example.android_loop.utils

import android.content.Context
import android.net.Uri
import com.example.android_loop.data.model_dataClass.productoResult.ImageRequest
import java.text.Normalizer

fun String.sinAcentos(): String {
    val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
    return normalized.replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
}

fun convertirListImgToListB64(imageUris: List<Uri>, context: Context): List<ImageRequest> {

    val imageRequest = mutableListOf<ImageRequest>()

    imageUris.forEachIndexed { index, uri ->

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



