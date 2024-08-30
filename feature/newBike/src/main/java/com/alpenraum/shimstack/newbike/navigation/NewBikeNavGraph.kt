package com.alpenraum.shimstack.newbike.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.alpenraum.shimstack.navigation.NavDestinationDefinition
import com.alpenraum.shimstack.navigation.NavGraphDefinition
import com.alpenraum.shimstack.newbike.NewBikeFeature

object NewBikeNavGraph : NavGraphDefinition {
    override val route = "newBike"
    override val startDestinationRoute: String = NewBike.route

    override val destinations: List<NavDestinationDefinition> =
        listOf(
            NewBike
        )

    object NewBike : NavDestinationDefinition {
        override val route = "NewBike"

        override fun buildRoute(
            navGraphBuilder: NavGraphBuilder,
            navController: NavController
        ) {
            navGraphBuilder.composable(route) { _ ->
                NewBikeFeature(navController)
            }
        }
    }
}