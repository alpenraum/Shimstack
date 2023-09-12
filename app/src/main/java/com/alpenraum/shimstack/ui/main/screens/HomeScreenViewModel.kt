package com.alpenraum.shimstack.ui.main.screens

import com.alpenraum.shimstack.data.bike.Bike
import com.alpenraum.shimstack.data.bike.BikeDTO
import com.alpenraum.shimstack.data.bike.Pressure
import com.alpenraum.shimstack.data.bike.Tire
import com.alpenraum.shimstack.data.cardsetup.CardSetup
import com.alpenraum.shimstack.ui.base.BaseViewModel
import com.alpenraum.shimstack.ui.base.UnidirectionalViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeScreenViewModel @Inject constructor() :
    BaseViewModel(), HomeScreenContract {

    private val mutableState = MutableStateFlow(
        HomeScreenContract.State(
            listOf(null),
            CardSetup.defaultConfig()
        )
    )
    override val state: StateFlow<HomeScreenContract.State> =
        mutableState.asStateFlow()

    private val eventFlow = MutableSharedFlow<HomeScreenContract.Event>()
    override val event: SharedFlow<HomeScreenContract.Event> =
        eventFlow.asSharedFlow()

    override fun intent(event: HomeScreenContract.Intent) = when (event) {
        HomeScreenContract.Intent.OnRefresh -> {}
        is HomeScreenContract.Intent.OnViewPagerSelectionChanged -> {
            onViewPagerSelectionChanged(event.page)
        }
    }

    private val testBikes = listOf(
        BikeDTO(
            name = "Bike1",
            frontTire = Tire(Pressure(23.0), 23.0, 23.0),
            rearTire = Tire(Pressure(23.0), 23.0, 23.0),
            type = Bike.Type.TRAIL,
            isEBike = false
        ),
        BikeDTO(
            name = "Bike2",
            frontTire = Tire(Pressure(23.0), 23.0, 23.0),
            rearTire = Tire(Pressure(23.0), 23.0, 23.0),
            type = Bike.Type.TRAIL,
            isEBike = false
        ),
        BikeDTO(
            name = "Bike3",
            frontTire = Tire(Pressure(23.0), 23.0, 23.0),
            rearTire = Tire(Pressure(23.0), 23.0, 23.0),
            type = Bike.Type.TRAIL,
            isEBike = false
        )
    )

    override fun onStart() {
        super.onStart()
        viewModelScope.launch {
            eventFlow.emit(HomeScreenContract.Event.Loading)
            mutableState.emit(state.value.copy(bikes = testBikes))
            eventFlow.emit(HomeScreenContract.Event.FinishedLoading)
        }
    }

    private fun onViewPagerSelectionChanged(page: Int) {
        viewModelScope.launch { eventFlow.emit(HomeScreenContract.Event.NewPageSelected) }
    }
}

interface HomeScreenContract :
    UnidirectionalViewModel<HomeScreenContract.State, HomeScreenContract.Intent, HomeScreenContract.Event> {

    data class State(val bikes: List<BikeDTO?>, val detailCardsSetup: List<CardSetup>) {
        fun getBike(page: Int) = bikes.getOrNull(page)
    }

    sealed class Event {
        object Loading : Event()
        object FinishedLoading : Event()
        object Error : Event()
        object NewPageSelected : Event()
    }

    sealed class Intent {
        object OnRefresh : Intent()
        class OnViewPagerSelectionChanged(val page: Int) : Intent()
    }
}

sealed class UIDataLabel {
    class Simple(val heading: String, val content: String) : UIDataLabel()
    class Complex(val data: Map<String, String>) : UIDataLabel()
}
