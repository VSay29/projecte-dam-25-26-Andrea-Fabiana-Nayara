package com.example.android_loop.utils

import java.text.Normalizer

fun String.sinAcentos(): String {
    val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
    return normalized.replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
}