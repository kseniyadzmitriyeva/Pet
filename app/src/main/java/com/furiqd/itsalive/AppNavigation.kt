package com.furiqd.itsalive

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.furiqd.auth.presentation.navigation.authGraph
import com.furiqd.auth.presentation.navigation.AuthGraph
import com.furiqd.home.presentation.navigation.HomeGraph
import com.furiqd.home.presentation.navigation.homeGraph
import com.furiqd.pairing.presentation.navigation.PairingGraph
import com.furiqd.pairing.presentation.navigation.pairingGraph
import com.furiqd.personalisation.presentation.navigation.personalisationGraph
import com.furiqd.personalisation.presentation.navigation.PersonalisationGraph

@Composable
fun AppNavigation(
    startGraph: Any = AuthGraph,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startGraph,
        modifier = modifier
    ) {
        authGraph(
            navController = navController,
            onAuthSuccess = { isOnboardingCompleted ->
                val targetDestination = if (isOnboardingCompleted) {
                    HomeGraph
                } else {
                    PersonalisationGraph
                }

                navController.navigate(targetDestination) {
                    popUpTo(AuthGraph) { inclusive = true }
                }
            }
        )

        personalisationGraph(
            navController = navController,
            onPersonalisationComplete = {
                navController.navigate(HomeGraph) {
                    popUpTo(PersonalisationGraph) { inclusive = true }
                }
            }
        )

        homeGraph(
            navController = navController,
            onNavigateToAuth = { navController.navigate(AuthGraph)},
            onNavigateToPairing = { navController.navigate(PairingGraph)}
        )

        pairingGraph(
            navController = navController,
            onNavigateToMain = { navController.navigate(HomeGraph)}
        )
    }
}