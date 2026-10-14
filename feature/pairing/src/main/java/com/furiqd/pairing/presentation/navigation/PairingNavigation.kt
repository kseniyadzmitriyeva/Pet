package com.furiqd.pairing.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object PairingGraph

@Serializable
data class HostDestination(val roomId: String)
