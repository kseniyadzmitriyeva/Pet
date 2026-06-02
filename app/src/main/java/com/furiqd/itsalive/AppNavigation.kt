package com.furiqd.itsalive

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.furiqd.auth.presentation.navigation.authGraph
import com.furiqd.auth.presentation.navigation.AuthGraph
import kotlinx.serialization.Serializable

@Serializable object AuthGraph
@Serializable object RootHomeGraph

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthGraph,
        modifier = modifier
    ) {
        authGraph(
            navController = navController,
            onAuthSuccess = {
                navController.navigate(RootHomeGraph) {
                    popUpTo(AuthGraph) { inclusive = true }
                }
            }
        )
    }
}