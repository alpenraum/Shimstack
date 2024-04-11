package com.alpenraum.allaboutclubs.features.navigation.pub

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

interface NavDestinationDefinition {
    val route: String

    fun buildRoute(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController
    )
}