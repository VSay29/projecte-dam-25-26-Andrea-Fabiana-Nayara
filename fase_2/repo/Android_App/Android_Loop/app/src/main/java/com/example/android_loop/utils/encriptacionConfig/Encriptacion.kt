package com.example.android_loop.utils.encriptacionConfig

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec

fun encriptarToken(token: String): String {
    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    cipher.init(Cipher.ENCRYPT_MODE, KeyProvider.getSecretKey())

    val iv = cipher.iv
    val encryptedBytes = cipher.doFinal(token.toByteArray(Charsets.UTF_8))

    return Base64.encodeToString(iv + encryptedBytes, Base64.NO_WRAP)
}

fun desencriptarToken(tokenEncriptado: String): String {
    return try {
        val decoded = Base64.decode(tokenEncriptado, Base64.NO_WRAP)
        val iv = decoded.sliceArray(0 until 12)
        val cipherBytes = decoded.sliceArray(12 until decoded.size)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(128, iv)

        cipher.init(Cipher.DECRYPT_MODE, KeyProvider.getSecretKey(), spec)

        val result = String(cipher.doFinal(cipherBytes), Charsets.UTF_8)
        result
    } catch (e: Exception) {
        ""
    }
}