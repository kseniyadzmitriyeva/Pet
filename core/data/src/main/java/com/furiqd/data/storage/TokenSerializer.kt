package com.furiqd.data.storage

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream

class TokenSerializer(private val cryptoManager: CryptoManager) : Serializer<String> {

    override val defaultValue: String = ""

    override suspend fun readFrom(input: InputStream): String {
        return try {
            String(cryptoManager.decrypt(input), Charsets.UTF_8)
        } catch (e: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: String, output: OutputStream) {
        cryptoManager.encrypt(output, t.toByteArray(Charsets.UTF_8))
    }
}