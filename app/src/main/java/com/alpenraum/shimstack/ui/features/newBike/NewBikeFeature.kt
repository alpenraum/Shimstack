package com.alpenraum.shimstack.ui.features.newBike

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alpenraum.shimstack.R
import com.alpenraum.shimstack.ui.base.use
import com.alpenraum.shimstack.ui.compose.AttachToLifeCycle
import com.alpenraum.shimstack.ui.compose.fadeIn
import com.alpenraum.shimstack.ui.compose.fadeOut
import com.alpenraum.shimstack.ui.features.NavGraphs
import com.alpenraum.shimstack.ui.features.destinations.EnterDetailsScreenDestination
import com.alpenraum.shimstack.ui.features.destinations.EnterSetupScreenDestination
import com.alpenraum.shimstack.ui.features.destinations.EntryScreenDestination
import com.alpenraum.shimstack.ui.features.destinations.SetupDecisionScreenDestination
import com.alpenraum.shimstack.ui.features.newBike.screens.EnterDetailsScreen
import com.alpenraum.shimstack.ui.features.newBike.screens.EnterSetupScreen
import com.alpenraum.shimstack.ui.features.newBike.screens.EntryScreen
import com.alpenraum.shimstack.ui.features.newBike.screens.SetupDecisionScreen
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.NestedNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
@Destination(
    navArgsDelegate = NewBikeNavArgs::class
)
@Composable
fun NewBikeFeature(
    navigator: DestinationsNavigator,
    viewModel: NewBikeViewModel = hiltViewModel()
) {
    AttachToLifeCycle(viewModel = viewModel)
    val (state, intent, event) = use(viewModel)

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.header_new_bike_entry),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        val navHostEngine =
            rememberAnimatedNavHostEngine(
                navHostContentAlignment = Alignment.TopCenter,
                rootDefaultAnimations =
                RootNavGraphDefaultAnimations(
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() }
                ),
                defaultAnimationsForNestedNavGraph =
                mapOf(
                    NavGraphs.newBike to
                        NestedNavGraphDefaultAnimations(
                            enterTransition = { slideInHorizontally() },
                            exitTransition = { slideOutHorizontally() },
                            popEnterTransition = { slideInHorizontally() },
                            popExitTransition = { slideOutHorizontally() }
                        )
                )
            )

        DestinationsNavHost(
            navGraph = NavGraphs.newBike,
            engine = navHostEngine
        ) {
            composable(EntryScreenDestination) {
                EntryScreen(
                    navigator = this.destinationsNavigator,
                    state = state,
                    intent = intent,
                    event = event
                )
            }
            composable(EnterDetailsScreenDestination) {
                EnterDetailsScreen(
                    navigator = this.destinationsNavigator,
                    state = state,
                    intent = intent,
                    event = event
                )
            }
            composable(SetupDecisionScreenDestination) {
                SetupDecisionScreen(
                    navigator = this.destinationsNavigator
                )
            }
            composable(EnterSetupScreenDestination) {
                EnterSetupScreen(
                    state,
                    intent,
                    event,
                    navigator = this.destinationsNavigator
                )
            }
        }
    }
}