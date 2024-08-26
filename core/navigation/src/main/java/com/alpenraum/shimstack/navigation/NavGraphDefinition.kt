package com.alpenraum.shimstack.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation

interface NavGraphDefinition {
    val route: String
    val startDestinationRoute: String

    val destinations: List<NavDestinationDefinition>

    fun build(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController
    ) {
        navGraphBuilder.navigation(
            route = route,
            startDestination = startDestinationRoute
        ) {
            destinations.forEach { destination ->
                destination.buildRoute(navGraphBuilder = this, navController)
            }
        }
    }
}