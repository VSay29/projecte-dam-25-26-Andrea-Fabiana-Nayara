package com.example.android_loop.utils.encriptacionConfig

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * La función de este object es generar y recuperar la clave secreta
 */

object KeyProvider {

    /**
     * ANDROID_KEYSTORE guarda la llave en un contenedor del sistema
     * al que otras apps no puedes acceder. Esto lo hace mucho más
     * seguro que guardarlo en un archivo de texto o en el código
     */
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val KEY_ALIAS = "ClaveTokenOdoo"

    /**
     *
     */

    internal fun getSecretKey(): SecretKey {

        /**
         * Primero se busca una clave si existe con el ALIAS para evitar
         * que no se pueda desencriptar lo que ya teníamos guardado
         */
        val keystore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        val existingKey = keystore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry
        if (existingKey != null) return existingKey.secretKey


        /**
         * Si no existe clave, se genera una con el algoritmo AES
         */
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        val spec = KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM) // BLOCK_MODE_GCM: Cifra y verifica que los datos no han sido manipulados
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()
        keyGenerator.init(spec)
        return keyGenerator.generateKey()

        /**
         * La clave de este object es que ni como programador, ni usuario como root, pueden obtener esta llave
         */
    }

}