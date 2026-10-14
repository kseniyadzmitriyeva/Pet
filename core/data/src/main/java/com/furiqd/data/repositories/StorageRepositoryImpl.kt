package com.furiqd.data.repositories

import com.furiqd.shared.repository.StorageRepository
import com.furiqd.network.contract.RemoteImageDataSource
import java.util.UUID
import javax.inject.Inject

class StorageRepositoryImpl@Inject constructor(
    private val imageRemoteDataSource: RemoteImageDataSource,
): StorageRepository {

    override suspend fun uploadImage(
        bytes: ByteArray,
        path: String,
        bucketKey: String
    ): Result<String> {
        val fileName = "${path}/${UUID.randomUUID()}"
        runCatching {
            imageRemoteDataSource.uploadImage(
                imageBytes = bytes,
                fileName = fileName,
                bucketKey = bucketKey
            )
        } .onFailure {
            return Result.failure(it)
        }
        return Result.success(fileName)
    }

    override suspend fun downloadImage(path: String): Result<ByteArray> {
        runCatching {
            imageRemoteDataSource.downloadImage(path)
        }.onFailure {
            return Result.failure(it)
            }
        return Result.success(ByteArray(0))
    }
}