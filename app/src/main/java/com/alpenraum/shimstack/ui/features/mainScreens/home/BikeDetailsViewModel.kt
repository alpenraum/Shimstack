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
class BikeDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
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

    private suspend fun saveBike() {
        if (updateBikeUseCase(_state.value.bike)) {
            _state.emit(_state.value.copy(editMode = false))
        } else {
            _event.emit(BikeDetailsContract.Event.ShowSnackbar(R.string.err_technical))
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

            is BikeDetailsContract.Intent.Input.FrontTireInternalRimWidth -> input.width.toDoubleOrNull()
                ?.let {
                    _state.emit(
                        BikeDetailsContract.State(
                            _state.value.bike.copy(
                                frontTire = _state.value.bike.frontTire.copy(
                                    internalRimWidthInMM = it
                                )
                            ),
                            true
                        )
                    )
                }

            is BikeDetailsContract.Intent.Input.FrontTirePressure -> input.pressure.toDoubleOrNull()
                ?.let {
                    _state.emit(
                        BikeDetailsContract.State(
                            _state.value.bike.copy(
                                frontTire = _state.value.bike.frontTire.copy(
                                    pressure = Pressure(it)
                                )
                            ),
                            true
                        )
                    )
                }

            is BikeDetailsContract.Intent.Input.FrontTireWidth -> input.width.toDoubleOrNull()
                ?.let {
                    _state.emit(
                        BikeDetailsContract.State(
                            _state.value.bike.copy(
                                frontTire = _state.value.bike.frontTire.copy(
                                    widthInMM = it
                                )
                            ),
                            true
                        )
                    )
                }

            is BikeDetailsContract.Intent.Input.RearTireInternalRimWidth -> input.width.toDoubleOrNull()
                ?.let {
                    _state.emit(
                        BikeDetailsContract.State(
                            _state.value.bike.copy(
                                rearTire = _state.value.bike.rearTire.copy(
                                    internalRimWidthInMM = it
                                )
                            ),
                            true
                        )
                    )
                }

            is BikeDetailsContract.Intent.Input.RearTirePressure -> input.pressure.toDoubleOrNull()
                ?.let {
                    _state.emit(
                        BikeDetailsContract.State(
                            _state.value.bike.copy(
                                rearTire = _state.value.bike.rearTire.copy(
                                    pressure = Pressure(it)
                                )
                            ),
                            true
                        )
                    )
                }

            is BikeDetailsContract.Intent.Input.RearTireWidth -> input.width.toDoubleOrNull()
                ?.let {
                    _state.emit(
                        BikeDetailsContract.State(
                            _state.value.bike.copy(
                                rearTire = _state.value.bike.rearTire.copy(
                                    widthInMM = it
                                )
                            ),
                            true
                        )
                    )
                }
        }
    }
}

interface BikeDetailsContract :
    UnidirectionalViewModel<BikeDetailsContract.State, BikeDetailsContract.Intent, BikeDetailsContract.Event> {
    @Immutable
    data class State(
        val bike: Bike,
        val editMode: Boolean = false
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
        }
    }
}

class BikeDetailsNavArgs(val bike: Bike)