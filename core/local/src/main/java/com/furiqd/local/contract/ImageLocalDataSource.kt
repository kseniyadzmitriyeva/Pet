package com.furiqd.local.contract

interface ImageLocalDataSource {
    suspend fun prepareImageBytes(rawBytes: ByteArray): ByteArray
}