package com.furiqd.pairing.presentation.navigation

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.furiqd.pairing.presentation.screen.hostroom.HostScreenRoute

fun NavGraphBuilder.pairingGraph(
    navController: NavController,
    onNavigateToMain: () -> Unit
) {
    navigation<PairingGraph>(startDestination = HostDestination) {
        composable<HostDestination> {
            Log.d("PairingGraph", "Navigating to HostScreenRoute")
            HostScreenRoute(
                onNavigateToMain = onNavigateToMain
            )
        }
    }
}