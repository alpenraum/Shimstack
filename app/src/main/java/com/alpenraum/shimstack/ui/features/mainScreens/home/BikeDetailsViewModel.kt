package com.alpenraum.shimstack.ui.features.mainScreens.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.alpenraum.shimstack.data.bike.Bike
import com.alpenraum.shimstack.data.bike.BikeDTO
import com.alpenraum.shimstack.ui.base.BaseViewModel
import com.alpenraum.shimstack.ui.base.UnidirectionalViewModel
import com.alpenraum.shimstack.ui.features.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BikeDetailsViewModel @Inject constructor(savedStateHandle: SavedStateHandle) :
    BaseViewModel(), BikeDetailsContract {

    private val navArgs = savedStateHandle.navArgs<BikeDetailsNavArgs>()

    private val _state: MutableStateFlow<BikeDetailsContract.State> = MutableStateFlow(
        BikeDetailsContract.State(navArgs.bike)
    )
    override val state: StateFlow<BikeDetailsContract.State>
        get() = _state.asStateFlow()

    private val _event: MutableSharedFlow<BikeDetailsContract.Event> = MutableSharedFlow()
    override val event: SharedFlow<BikeDetailsContract.Event>
        get() = _event.asSharedFlow()

    override fun intent(intent: BikeDetailsContract.Intent) {
        viewModelScope.launch {
            when (intent) {
                BikeDetailsContract.Intent.OnBackPressed -> _event.emit(
                    BikeDetailsContract.Event.NavigateBack
                )

                BikeDetailsContract.Intent.OnEditClicked -> _state.emit(
                    _state.value.copy(editMode = true)
                )

                is BikeDetailsContract.Intent.Input -> handleInput(intent)
            }
        }
    }

    private suspend fun handleInput(input: BikeDetailsContract.Intent.Input) {
        // TODO: Validation rules from EnterDetailsScreen
        when (input) {
            is BikeDetailsContract.Intent.Input.BikeName -> _state.emit(
                BikeDetailsContract.State(
                    _state.value.bike.copy(name = input.name),
                    true
                )
            )

            is BikeDetailsContract.Intent.Input.BikeType -> _state.emit(
                BikeDetailsContract.State(
                    _state.value.bike.copy(type = input.type),
                    true
                )
            )
        }
    }
}

interface BikeDetailsContract :
    UnidirectionalViewModel<BikeDetailsContract.State, BikeDetailsContract.Intent, BikeDetailsContract.Event> {
    @Immutable
    data class State(
        val bike: BikeDTO,
        val editMode: Boolean = false
    )

    sealed class Event {
        data object NavigateBack : Event()
    }

    sealed class Intent {
        data object OnEditClicked : Intent()
        data object OnBackPressed : Intent()

        sealed class Input : Intent() {
            class BikeName(val name: String) : Input()
            class BikeType(val type: Bike.Type) : Input()
        }
    }
}

class BikeDetailsNavArgs(val bike: BikeDTO)