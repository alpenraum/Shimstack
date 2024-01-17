package com.alpenraum.shimstack.ui.features.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alpenraum.shimstack.ui.compose.AttachToLifeCycle
import com.alpenraum.shimstack.ui.features.destinations.MainScreenFeatureDestination
import com.alpenraum.shimstack.ui.features.destinations.OnboardingFeatureDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import kotlinx.coroutines.flow.collectLatest

@RootNavGraph
@Destination
@Composable
fun OnboardingFeature(
    navigator: DestinationsNavigator,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    AttachToLifeCycle(viewModel = viewModel)

    LaunchedEffect(key1 = Unit) {
        viewModel.event().collectLatest {
            when (it) {
                OnboardingViewModel.Event.NavigateToHomeScreen -> navigator.navigate(
                    MainScreenFeatureDestination,
                    onlyIfResumed = true
                ) {
                    popUpTo(OnboardingFeatureDestination) {
                        inclusive = true
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        OnboardingScreen(
            onSkipButtonClicked = viewModel::onSkipClicked,
            modifier = Modifier.padding(it).padding(16.dp)
        ) {
            // TODO
        }
    }
}