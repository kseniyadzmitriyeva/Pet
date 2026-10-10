package com.furiqd.network.contract

interface RemoteImageDataSource {
    suspend fun uploadImage(imageBytes: ByteArray, fileName : String, bucketKey: String): Result<String>
    suspend fun downloadImage(path: String): Result<ByteArray>
}