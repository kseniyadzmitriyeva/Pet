package com.furiqd.pairing.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object PairingGraph

@Serializable
data class HostDestination(val roomId: String)

@Serializable
data object JoinRoomDestination

@Serializable
data object CreateRoomDestination

@Serializable
data object ChoiceDestination