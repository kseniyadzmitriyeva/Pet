package com.furiqd.domain.model

sealed interface RoomStatus {
    object WaitingForFriend : RoomStatus
    object Active : RoomStatus
    object Expired : RoomStatus
    object NotFound : RoomStatus
}