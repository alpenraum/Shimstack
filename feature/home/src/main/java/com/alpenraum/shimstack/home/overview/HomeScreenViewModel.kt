package com.alpenraum.shimstack.home.overview

import androidx.compose.runtime.Immutable
import androidx.navigation.NavController
import com.alpenraum.shimstack.common.DispatchersProvider
import com.alpenraum.shimstack.data.bike.LocalBikeRepository
import com.alpenraum.shimstack.home.navigation.HomeNavigator
import com.alpenraum.shimstack.model.bike.Bike
import com.alpenraum.shimstack.model.cardsetup.CardSetup
import com.alpenraum.shimstack.newbike.navigation.NewBikeNavigator
import com.alpenraum.shimstack.ui.base.BaseViewModel
import com.alpenraum.shimstack.ui.base.UnidirectionalViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel
    @Inject
    constructor(
        private val bikeRepository: LocalBikeRepository,
        private val homeNavigator: HomeNavigator,
        private val newBikeNavigator: NewBikeNavigator,
        dispatchersProvider: DispatchersProvider
    ) : BaseViewModel(dispatchersProvider),
        HomeScreenContract {
        private val mutableState =
            MutableStateFlow(
                HomeScreenContract.State(
                    persistentListOf(null),
                    CardSetup.defaultConfig()
                )
            )
        override val state: StateFlow<HomeScreenContract.State> =
            mutableState.asStateFlow()

        private val eventFlow = MutableSharedFlow<HomeScreenContract.Event>()
        override val event: SharedFlow<HomeScreenContract.Event> =
            eventFlow.asSharedFlow()

        override fun intent(
            intent: HomeScreenContract.Intent,
            navController: NavController
        ) {
            when (intent) {
                HomeScreenContract.Intent.OnRefresh -> {}
                is HomeScreenContract.Intent.OnViewPagerSelectionChanged -> {
                    onViewPagerSelectionChanged()
                }

                HomeScreenContract.Intent.OnAddNewBike -> {
                    viewModelScope.launch {
                        newBikeNavigator.navigateToNewBike(navController)
                    }
                }

                is HomeScreenContract.Intent.OnBikeDetailsClicked ->
                    viewModelScope.launch {
                        intent.bike.id?.let { homeNavigator.navigateToBikeDetails(navController, it) }
                    }

                HomeScreenContract.Intent.OnTechnicalError ->
                    viewModelScope.launch {
                        eventFlow.emit(HomeScreenContract.Event.Error)
                    }
            }
        }

        override fun onStart() {
            super.onStart()
            iOScope.launch {
                eventFlow.emit(HomeScreenContract.Event.Loading)

                mutableState.emit(state.value.copy(bikes = fetchBikes().toImmutableList()))
                eventFlow.emit(HomeScreenContract.Event.FinishedLoading)
            }
        }

        private suspend fun fetchBikes() = bikeRepository.getAllBikes()

        private fun onViewPagerSelectionChanged() {
            viewModelScope.launch { eventFlow.emit(HomeScreenContract.Event.NewPageSelected) }
        }
    }

interface HomeScreenContract : UnidirectionalViewModel<HomeScreenContract.State, HomeScreenContract.Intent, HomeScreenContract.Event> {
    @Immutable
    data class State(
        val bikes: ImmutableList<Bike?>,
        val detailCardsSetup: ImmutableList<CardSetup>
    ) {
        fun getBike(page: Int) = bikes.getOrNull(page)
    }

    sealed class Event {
        data object Loading : Event()

        data object FinishedLoading : Event()

        data object Error : Event()

        data object NewPageSelected : Event()
    }

    sealed class Intent {
        data object OnRefresh : Intent()

        data object OnViewPagerSelectionChanged : Intent()

        data object OnAddNewBike : Intent()

        data class OnBikeDetailsClicked(
            val bike: Bike
        ) : Intent()

        data object OnTechnicalError : Intent()
    }
}

sealed class UIDataLabel {
    class Simple(
        val heading: String,
        val content: String
    ) : UIDataLabel()

    class Complex(
        val data: Map<String, String>
    ) : UIDataLabel()
}