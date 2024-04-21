package com.alpenraum.shimstack.common.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

// TODO: everything here

// @HiltViewModel
class NavViewModel
//  @Inject
    constructor(
        @StartDestinationRoute val startDestinationRoute: String,
        val destinationBuilders: Set<@JvmSuppressWildcards NavGraphDefinition>
    ) : ViewModel()

@Composable
fun ShimstackNavHost(
    navController: NavHostController,
    viewModel: NavViewModel = hiltViewModel()
) {
    val destinationBuilders = viewModel.destinationBuilders

    NavHost(
        navController = navController,
        startDestination = viewModel.startDestinationRoute
    ) {
        destinationBuilders.forEach { navGraphDefinition ->
            navGraphDefinition.build(navGraphBuilder = this, navController)
        }
    }
}