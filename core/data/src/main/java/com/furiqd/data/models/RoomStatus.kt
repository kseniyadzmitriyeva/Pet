package com.furiqd.data.models

sealed interface RoomStatus {
    object WaitingForFriend : RoomStatus
    object Active : RoomStatus
    object Expired : RoomStatus
    object NotFound : RoomStatus
}