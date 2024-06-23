package com.alpenraum.shimstack.onboarding

import androidx.navigation.NavController
import com.alpenraum.shimstack.common.DispatchersProvider
import com.alpenraum.shimstack.datastore.ShimstackDatastore
import com.alpenraum.shimstack.home.navigation.HomeNavigator
import com.alpenraum.shimstack.navigation.popUpToNavOptions
import com.alpenraum.shimstack.newbike.navigation.NewBikeNavigator
import com.alpenraum.shimstack.onboarding.navigation.OnboardingNavGraph
import com.alpenraum.shimstack.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel
    @Inject
    constructor(
        dispatchersProvider: DispatchersProvider,
        private val dataStore: ShimstackDatastore,
        private val homeNavigator: HomeNavigator,
        private val newBikeNavigator: NewBikeNavigator
    ) : BaseViewModel(dispatchersProvider) {
        @Suppress("ktlint:standard:backing-property-naming")
        private val _event = MutableSharedFlow<Event>()

        fun event() = _event.asSharedFlow()

        sealed class Event {
            data object NavigateToHomeScreen : Event()
        }

        fun onHomeNavigationClicked(navController: NavController) {
            val options = popUpToNavOptions(OnboardingNavGraph.route)

            homeNavigator.navigateToHome(navController, navOptions = options)
        }

        fun onAddBikeNavigationClicked(navController: NavController) {
            newBikeNavigator.navigateToNewBike(navController)
        }

        fun onSkipClicked() {
            viewModelScope.launch {
                dataStore.setIsOnboardingCompleted(true)
                _event.emit(Event.NavigateToHomeScreen)
            }
        }
    }