package com.alpenraum.shimstack.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// TODO: everything here

@HiltViewModel
class NavViewModel
    @Inject
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