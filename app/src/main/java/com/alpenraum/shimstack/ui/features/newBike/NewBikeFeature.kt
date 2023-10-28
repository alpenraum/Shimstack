package com.alpenraum.shimstack.ui.features.newBike

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.alpenraum.shimstack.common.popBackstackOrNavigate
import com.alpenraum.shimstack.ui.base.use
import com.alpenraum.shimstack.ui.compose.AttachToLifeCycle
import com.alpenraum.shimstack.ui.features.newBike.screens.EntryScreen
import dev.olshevski.navigation.reimagined.AnimatedNavHost
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.hilt.hiltViewModel
import dev.olshevski.navigation.reimagined.rememberNavController

@Composable
fun NewBikeFeature() {
    val navController = rememberNavController<NewBikeDestinations>(
        startDestination = NewBikeDestinations.Entry
    )
    NavBackHandler(controller = navController)
    val viewModel: NewBikeViewModel = hiltViewModel()
    AttachToLifeCycle(viewModel = viewModel)
    val (state, intent, event) = use(viewModel)

    LaunchedEffect(key1 = state) {
        val destination: NewBikeDestinations = when (state) {
            is NewBikeContract.State.Entry -> NewBikeDestinations.Entry
        }

        navController.popBackstackOrNavigate(destination)
    }

    AnimatedNavHost(controller = navController) {
        when (it) {
            is NewBikeDestinations.Entry -> EntryScreen(
                state = state as NewBikeContract.State.Entry,
                intent = intent
            )
        }
    }
}
