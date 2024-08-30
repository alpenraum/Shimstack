package com.alpenraum.shimstack.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.alpenraum.shimstack.navigation.NavDestinationDefinition
import com.alpenraum.shimstack.navigation.NavGraphDefinition
import com.alpenraum.shimstack.onboarding.OnboardingFeature

object OnboardingNavGraph : NavGraphDefinition {
    override val route = "onboarding"
    override val startDestinationRoute: String = Welcome.route

    override val destinations: List<NavDestinationDefinition> =
        listOf(
            Welcome
        )

    object Welcome : NavDestinationDefinition {
        override val route = "welcome"

        override fun buildRoute(
            navGraphBuilder: NavGraphBuilder,
            navController: NavController
        ) {
            navGraphBuilder.composable(route) { _ ->
                OnboardingFeature(navController)
            }
        }
    }
}