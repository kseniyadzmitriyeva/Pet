package com.furiqd.domain.repository

interface StorageRepository {
    suspend fun uploadImage(bytes: ByteArray, path: String, bucketKey: String): Result<String>
    suspend fun downloadImage(path: String): Result<ByteArray>
}