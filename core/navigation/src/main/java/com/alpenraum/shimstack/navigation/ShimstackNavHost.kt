package com.alpenraum.shimstack.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

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
    modifier: Modifier = Modifier,
    viewModel: NavViewModel = hiltViewModel()
) {
    val destinationBuilders = viewModel.destinationBuilders

    NavHost(
        navController = navController,
        startDestination = viewModel.startDestinationRoute,
        modifier = modifier
    ) {
        destinationBuilders.forEach { navGraphDefinition ->
            navGraphDefinition.build(navGraphBuilder = this, navController)
        }
    }
}