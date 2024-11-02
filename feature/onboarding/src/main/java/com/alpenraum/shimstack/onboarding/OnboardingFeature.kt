package com.alpenraum.shimstack.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alpenraum.shimstack.ui.compose.components.AttachToLifeCycle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun OnboardingFeature(
    navigator: NavController,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    AttachToLifeCycle(viewModel = viewModel)

    LaunchedEffect(key1 = Unit) {
        viewModel.event().collectLatest {
            when (it) {
                OnboardingViewModel.Event.NavigateToHomeScreen -> {
                    viewModel.onHomeNavigationClicked(navigator)
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
            modifier =
            Modifier
                .padding(it)
                .padding(16.dp)
        ) {
            viewModel.onAddBikeNavigationClicked(navigator)
        }
    }
}