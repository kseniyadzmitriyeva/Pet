package com.furiqd.pairing.presentation.navigation

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.furiqd.pairing.presentation.screen.createroom.CreateRoomRoute
import com.furiqd.pairing.presentation.screen.hostroom.HostScreenRoute
import com.furiqd.pairing.presentation.screen.joinroom.JoinRoomRoute
import com.furiqd.pairing.presentation.screen.pairingchoice.PairingChoiceRoute

fun NavGraphBuilder.pairingGraph(
    navController: NavController,
    onNavigateToMain: () -> Unit
) {
    navigation<PairingGraph>(startDestination =ChoiceDestination) {
        composable<HostDestination> {
            HostScreenRoute(
                onNavigateToMain = onNavigateToMain
            )
        }

        composable<JoinRoomDestination> {
            JoinRoomRoute(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToRoom = { roomId ->
                    navController.navigate(HostDestination(roomId))
                }
            )
        }

        composable<CreateRoomDestination> {
            CreateRoomRoute(
                onNavigateToMain = { onNavigateToMain() }
            )
        }

        composable<ChoiceDestination> {
            PairingChoiceRoute(
                onNavigateToCreate = { navController.navigate(CreateRoomDestination) },
                onNavigateToJoin = { navController.navigate(JoinRoomDestination) },
                onDismiss = { navController.popBackStack() }
            )
        }
    }
}