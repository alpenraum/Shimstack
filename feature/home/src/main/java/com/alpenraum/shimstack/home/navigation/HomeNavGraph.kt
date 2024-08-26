package com.alpenraum.shimstack.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.alpenraum.shimstack.home.MainScreenFeature
import com.alpenraum.shimstack.home.bikedetails.BikeDetailsScreen
import com.alpenraum.shimstack.navigation.NavDestinationDefinition
import com.alpenraum.shimstack.navigation.NavGraphDefinition

object HomeNavGraph : NavGraphDefinition {
    override val route = "home"
    override val startDestinationRoute: String = Overview.route

    override val destinations: List<NavDestinationDefinition> =
        listOf(
            Overview,
            BikeDetails
        )

    object Overview : NavDestinationDefinition {
        override val route = "main"

        override fun buildRoute(
            navGraphBuilder: NavGraphBuilder,
            navController: NavController
        ) {
            navGraphBuilder.composable(route) { _ ->
                MainScreenFeature(navController)
            }
        }
    }

    object BikeDetails : NavDestinationDefinition {
        const val ARGUMENT_ID = "id"
        override val route = "details"

        override fun buildRoute(
            navGraphBuilder: NavGraphBuilder,
            navController: NavController
        ) {
            navGraphBuilder.composable(route) { _ ->
                BikeDetailsScreen(navController)
            }
        }
    }
}