package com.alpenraum.shimstack.ui.screens.newBike

import androidx.compose.runtime.Composable
import dev.olshevski.navigation.reimagined.AnimatedNavHost
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.rememberNavController

@Composable
fun NewBikeFeature() {
    val navController = rememberNavController<NewBikeDestinations>(
        startDestination = NewBikeDestinations.Entry
    )
    NavBackHandler(controller = navController)

    AnimatedNavHost(controller = navController) {
        when (it) {
            NewBikeDestinations.Entry -> { /*TODO*/
            }
        }
    }
}
