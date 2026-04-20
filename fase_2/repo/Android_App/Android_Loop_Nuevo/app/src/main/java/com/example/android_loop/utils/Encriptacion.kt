package com.example.android_loop.utils

import java.security.MessageDigest

fun encriptarPasswd(passwd: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(passwd.toByteArray(Charsets.UTF_8))
    return hashBytes.fold("") { str, byte -> str + "%02x".format(byte) }
}

fun encriptarToken(token: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(token.toByteArray(Charsets.UTF_8))
    return hashBytes.fold("") { str, byte -> str + "%02x".format(byte) }
}