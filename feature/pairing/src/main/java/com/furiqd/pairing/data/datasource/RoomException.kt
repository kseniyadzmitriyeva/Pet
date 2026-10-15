package com.furiqd.pairing.data.datasource

sealed class RoomException(message: String) : Exception(message) {
    class RoomNotFoundException : RoomException("ROOM_NOT_FOUND")
    class RoomAlreadyOccupiedException : RoomException("ROOM_ALREADY_OCCUPIED")
}