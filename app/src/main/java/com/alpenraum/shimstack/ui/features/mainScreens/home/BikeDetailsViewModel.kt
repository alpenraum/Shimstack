package com.alpenraum.shimstack.ui.features.mainScreens.home

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.alpenraum.shimstack.R
import com.alpenraum.shimstack.data.bike.Bike
import com.alpenraum.shimstack.data.bike.BikeDTO
import com.alpenraum.shimstack.data.bike.Pressure
import com.alpenraum.shimstack.ui.base.BaseViewModel
import com.alpenraum.shimstack.ui.base.UnidirectionalViewModel
import com.alpenraum.shimstack.ui.features.navArgs
import com.alpenraum.shimstack.usecases.UpdateBikeUseCase
import com.alpenraum.shimstack.usecases.ValidateBikeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BikeDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val validateBikeUseCase: ValidateBikeUseCase,
    private val updateBikeUseCase: UpdateBikeUseCase
) :
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
                BikeDetailsContract.Intent.OnSaveClicked -> saveBike()
            }
        }
    }

    private suspend fun saveBike() = withContext(Dispatchers.IO) {
        if (updateBikeUseCase(_state.value.bike)) {
            _state.emit(_state.value.copy(editMode = false))
        } else {
            _event.emit(BikeDetailsContract.Event.ShowSnackbar(R.string.err_technical))
        }
    }

    private suspend fun handleInput(input: BikeDetailsContract.Intent.Input) {
        when (input) {
            is BikeDetailsContract.Intent.Input.BikeName -> {
                val newBike = _state.value.bike.copy(name = input.name)
                emitNewState(newBike)
            }

            is BikeDetailsContract.Intent.Input.BikeType -> {
                val newBike = _state.value.bike.copy(type = input.type)
                emitNewState(newBike)
            }

            is BikeDetailsContract.Intent.Input.FrontTireInternalRimWidth -> {
                val width = input.width.toDoubleOrNull() ?: 0.0
                val newBike = _state.value.bike.copy(
                    frontTire = _state.value.bike.frontTire.copy(
                        internalRimWidthInMM = width
                    )
                )
                emitNewState(newBike)
            }

            is BikeDetailsContract.Intent.Input.FrontTirePressure -> {
                val pressure = input.pressure.toDoubleOrNull() ?: 0.0
                val newBike = _state.value.bike.copy(
                    frontTire = _state.value.bike.frontTire.copy(
                        pressure = Pressure(pressure)
                    )
                )
                emitNewState(newBike)
            }

            is BikeDetailsContract.Intent.Input.FrontTireWidth -> {
                val width = input.width.toDoubleOrNull() ?: 0.0
                val newBike = _state.value.bike.copy(
                    frontTire = _state.value.bike.frontTire.copy(
                        widthInMM = width
                    )
                )
                emitNewState(newBike)
            }

            is BikeDetailsContract.Intent.Input.RearTireInternalRimWidth -> {
                val width = input.width.toDoubleOrNull() ?: 0.0
                val newBike = _state.value.bike.copy(
                    rearTire = _state.value.bike.rearTire.copy(
                        internalRimWidthInMM = width
                    )
                )
                emitNewState(newBike)
            }

            is BikeDetailsContract.Intent.Input.RearTirePressure -> {
                val pressure = input.pressure.toDoubleOrNull() ?: 0.0
                val newBike = _state.value.bike.copy(
                    rearTire = _state.value.bike.rearTire.copy(
                        pressure = Pressure(pressure)
                    )
                )
                emitNewState(newBike)
            }

            is BikeDetailsContract.Intent.Input.RearTireWidth -> {
                val width = input.width.toDoubleOrNull() ?: 0.0
                val newBike = _state.value.bike.copy(
                    rearTire = _state.value.bike.rearTire.copy(
                        widthInMM = width
                    )
                )
                emitNewState(newBike)
            }

            is BikeDetailsContract.Intent.Input.FrontSuspensionPressure -> {
                val pressure = input.pressure.toDoubleOrNull() ?: 0.0
                val newBike = _state.value.bike.copy(
                    frontSuspension = _state.value.bike.frontSuspension?.copy(
                        pressure = Pressure(pressure)
                    )
                )
                emitNewState(newBike)
            }

            is BikeDetailsContract.Intent.Input.FrontSuspensionTokens -> {
                val tokens = input.tokens.toIntOrNull() ?: 0
                val newBike = _state.value.bike.copy(
                    frontSuspension = _state.value.bike.frontSuspension?.copy(
                        tokens = tokens
                    )
                )
                emitNewState(newBike)
            }

            is BikeDetailsContract.Intent.Input.FrontSuspensionTravel -> {
                val travel = input.travel.toIntOrNull() ?: 0
                val newBike = _state.value.bike.copy(
                    frontSuspension = _state.value.bike.frontSuspension?.copy(
                        travel = travel
                    )
                )
                emitNewState(newBike)
            }

            is BikeDetailsContract.Intent.Input.RearSuspensionPressure -> {
                val pressure = input.pressure.toDoubleOrNull() ?: 0.0
                val newBike = _state.value.bike.copy(
                    rearSuspension = _state.value.bike.rearSuspension?.copy(
                        pressure = Pressure(pressure)
                    )
                )
                emitNewState(newBike)
            }

            is BikeDetailsContract.Intent.Input.RearSuspensionTokens -> {
                val tokens = input.tokens.toIntOrNull() ?: 0
                val newBike = _state.value.bike.copy(
                    rearSuspension = _state.value.bike.rearSuspension?.copy(
                        tokens = tokens
                    )
                )
                emitNewState(newBike)
            }

            is BikeDetailsContract.Intent.Input.RearSuspensionTravel -> {
                val travel = input.travel.toIntOrNull() ?: 0
                val newBike = _state.value.bike.copy(
                    rearSuspension = _state.value.bike.rearSuspension?.copy(
                        travel = travel
                    )
                )
                emitNewState(newBike)
            }
        }
    }

    private suspend fun emitNewState(bike: Bike, editMode: Boolean = true) {
        _state.emit(
            BikeDetailsContract.State(
                bike,
                editMode,
                validationFailure = validateBikeUseCase(bike) as? ValidateBikeUseCase.DetailsFailure
            )
        )
    }
}

interface BikeDetailsContract :
    UnidirectionalViewModel<BikeDetailsContract.State, BikeDetailsContract.Intent, BikeDetailsContract.Event> {
    @Immutable
    data class State(
        val bike: Bike,
        val editMode: Boolean = false,
        val validationFailure: ValidateBikeUseCase.DetailsFailure? = null
    )

    sealed class Event {
        data object NavigateBack : Event()

        data class ShowSnackbar(@StringRes val message: Int) : Event()
    }

    sealed class Intent {
        data object OnEditClicked : Intent()
        data object OnBackPressed : Intent()

        data object OnSaveClicked : Intent()

        sealed class Input : Intent() {
            class BikeName(val name: String) : Input()
            class BikeType(val type: BikeDTO.Type) : Input()
            class FrontTirePressure(val pressure: String) : Input()
            class FrontTireWidth(val width: String) : Input()
            class FrontTireInternalRimWidth(val width: String) : Input()

            class RearTirePressure(val pressure: String) : Input()
            class RearTireWidth(val width: String) : Input()
            class RearTireInternalRimWidth(val width: String) : Input()

            class FrontSuspensionTravel(val travel: String) : Input()
            class RearSuspensionTravel(val travel: String) : Input()

            class FrontSuspensionPressure(val pressure: String) : Input()
            class RearSuspensionPressure(val pressure: String) : Input()

            class FrontSuspensionTokens(val tokens: String) : Input()
            class RearSuspensionTokens(val tokens: String) : Input()
        }
    }
}

class BikeDetailsNavArgs(val bike: Bike)