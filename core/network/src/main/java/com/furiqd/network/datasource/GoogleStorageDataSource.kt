package com.furiqd.network.datasource

import com.furiqd.network.contract.RemoteImageDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Named

class GoogleStorageDataSource @Inject constructor(
    @Named("workerEndpoint") private val workerEndpoint: String,
    private val okHttpClient: OkHttpClient
) : RemoteImageDataSource {

    override suspend fun uploadImage(
        imageBytes: ByteArray,
        fileName: String,
        bucketKey: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val url = workerEndpoint.toHttpUrl().newBuilder()
                .addQueryParameter("name", fileName)
                .addQueryParameter("bucket", bucketKey)
                .build()

            val request = Request.Builder()
                .url(url)
                .post(imageBytes.toRequestBody("image/jpeg".toMediaType()))
                .build()

            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.failure(
                        Exception("Upload failed: ${response.code} ${response.message}")
                    )
                }
                val responseBody = response.body?.string() ?: fileName
                Result.success(responseBody)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun downloadImage(path: String): Result<ByteArray> = withContext(Dispatchers.IO) {
        try {
            val url = workerEndpoint.toHttpUrl().newBuilder()
                .addQueryParameter("name", path)
                .build()

            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.failure(
                        Exception("Download failed: ${response.code}")
                    )
                }
                val bytes = response.body?.bytes() ?: ByteArray(0)
                Result.success(bytes)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}