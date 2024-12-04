package com.alpenraum.shimstack.home.bikedetails

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import com.alpenraum.shimstack.bikeservice.UpdateBikeUseCase
import com.alpenraum.shimstack.common.DispatchersProvider
import com.alpenraum.shimstack.data.bike.BikeRepository
import com.alpenraum.shimstack.home.navigation.HomeNavGraph
import com.alpenraum.shimstack.home.usecases.ValidateBikeUseCase
import com.alpenraum.shimstack.model.bike.Bike
import com.alpenraum.shimstack.model.bike.BikeType
import com.alpenraum.shimstack.model.measurementunit.Distance
import com.alpenraum.shimstack.model.measurementunit.MeasurementUnitType
import com.alpenraum.shimstack.model.measurementunit.Pressure
import com.alpenraum.shimstack.ui.base.BaseViewModel
import com.alpenraum.shimstack.ui.base.UnidirectionalViewModel
import com.alpenraum.shimstack.usersettingsdomain.GetUserSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.alpenraum.shimstack.ui.R as CommonR

@HiltViewModel
class BikeDetailsViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val validateBikeUseCase: ValidateBikeUseCase,
    private val updateBikeUseCase: UpdateBikeUseCase,
    private val bikeRepository: BikeRepository,
    userSettingsUseCase: GetUserSettingsUseCase,
    dispatchersProvider: DispatchersProvider
) : BaseViewModel(dispatchersProvider), BikeDetailsContract {
    private val selectedBikeId: Int = savedStateHandle[HomeNavGraph.BikeDetails.ARGUMENT_ID] ?: -1

    private val bikeFlow: MutableStateFlow<Bike> = MutableStateFlow(Bike.empty())
    private val editModeFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<BikeDetailsContract.State> =
        combine(bikeFlow, editModeFlow, userSettingsUseCase()) { bike, editMode, measurementUnit ->
            Triple(bike, editMode, measurementUnit)
        }.mapLatest { emitNewState(it.first, it.second, it.third.measurementUnitType) }
            .inViewModelScope(BikeDetailsContract.State.Details(Bike.empty()))

    private val _event: MutableSharedFlow<BikeDetailsContract.Event> = MutableSharedFlow()
    override val event: SharedFlow<BikeDetailsContract.Event>
        get() = _event.asSharedFlow()

    override fun onStart() {
        super.onStart()
        iOScope.launch {
            bikeRepository.getBike(selectedBikeId)?.let { newBike ->
                bikeFlow.update { newBike }
            }
        }
    }

    override fun intent(
        intent: BikeDetailsContract.Intent,
        navController: NavController
    ) {
        viewModelScope.launch {
            when (intent) {
                BikeDetailsContract.Intent.OnBackPressed -> navController.popBackStack()

                BikeDetailsContract.Intent.OnEditClicked -> editModeFlow.update { true }

                is BikeDetailsContract.Intent.Input -> handleInput(intent)
                BikeDetailsContract.Intent.OnSaveClicked -> saveBike()
            }
        }
    }

    private suspend fun saveBike() =
        withContext(Dispatchers.IO) {
            if (updateBikeUseCase(bikeFlow.value)) {
                editModeFlow.update { false }
            } else {
                _event.emit(BikeDetailsContract.Event.ShowSnackbar(CommonR.string.err_technical))
            }
        }

    private suspend fun handleInput(input: BikeDetailsContract.Intent.Input) =
        (state.value as? BikeDetailsContract.State.Edit)?.let {
            when (input) {
                is BikeDetailsContract.Intent.Input.BikeName -> {
                    bikeFlow.update { it.copy(name = input.name) }
                }

                is BikeDetailsContract.Intent.Input.BikeType -> {
                    bikeFlow.update { it.copy(type = input.type) }
                }

                is BikeDetailsContract.Intent.Input.FrontTireInternalRimWidth -> {
                    val width = input.width.toDoubleOrNull() ?: 0.0
                    bikeFlow.update {
                        it.copy(
                            frontTire =
                                it.frontTire.copy(
                                    internalRimWidthInMM = Distance(width)
                                )
                        )
                    }
                }

                is BikeDetailsContract.Intent.Input.FrontTirePressure -> {
                    val pressure = input.pressure.toDoubleOrNull() ?: 0.0
                    bikeFlow.update {
                        it.copy(
                            frontTire =
                                it.frontTire.copy(
                                    pressure = Pressure(pressure)
                                )
                        )
                    }
                }

                is BikeDetailsContract.Intent.Input.FrontTireWidth -> {
                    val width = input.width.toDoubleOrNull() ?: 0.0
                    bikeFlow.update {
                        it.copy(
                            frontTire =
                                it.frontTire.copy(
                                    width = Distance(width)
                                )
                        )
                    }
                }

                is BikeDetailsContract.Intent.Input.RearTireInternalRimWidth -> {
                    val width = input.width.toDoubleOrNull() ?: 0.0
                    bikeFlow.update {
                        it.copy(
                            rearTire =
                                it.rearTire.copy(
                                    internalRimWidthInMM = Distance(width)
                                )
                        )
                    }
                }

                is BikeDetailsContract.Intent.Input.RearTirePressure -> {
                    val pressure = input.pressure.toDoubleOrNull() ?: 0.0
                    bikeFlow.update {
                        it.copy(
                            rearTire =
                                it.rearTire.copy(
                                    pressure = Pressure(pressure)
                                )
                        )
                    }
                }

                is BikeDetailsContract.Intent.Input.RearTireWidth -> {
                    val width = input.width.toDoubleOrNull() ?: 0.0
                    bikeFlow.update {
                        it.copy(
                            rearTire =
                                it.rearTire.copy(
                                    width = Distance(width)
                                )
                        )
                    }
                }

                is BikeDetailsContract.Intent.Input.FrontSuspensionPressure -> {
                    val pressure = input.pressure.toDoubleOrNull() ?: 0.0
                    bikeFlow.update {
                        it.copy(
                            frontSuspension =
                                it.frontSuspension?.copy(
                                    pressure = Pressure(pressure)
                                )
                        )
                    }
                }

                is BikeDetailsContract.Intent.Input.FrontSuspensionTokens -> {
                    val tokens = input.tokens.toIntOrNull() ?: 0
                    bikeFlow.update {
                        it.copy(
                            frontSuspension =
                                it.frontSuspension?.copy(
                                    tokens = tokens
                                )
                        )
                    }
                }

                is BikeDetailsContract.Intent.Input.FrontSuspensionTravel -> {
                    val travel = input.travel.toDoubleOrNull() ?: 0.0
                    bikeFlow.update {
                        it.copy(
                            frontSuspension =
                                it.frontSuspension?.copy(
                                    travel = Distance(travel)
                                )
                        )
                    }
                }

                is BikeDetailsContract.Intent.Input.RearSuspensionPressure -> {
                    val pressure = input.pressure.toDoubleOrNull() ?: 0.0
                    bikeFlow.update {
                        it.copy(
                            rearSuspension =
                                it.rearSuspension?.copy(
                                    pressure = Pressure(pressure)
                                )
                        )
                    }
                }

                is BikeDetailsContract.Intent.Input.RearSuspensionTokens -> {
                    val tokens = input.tokens.toIntOrNull() ?: 0
                    bikeFlow.update {
                        it.copy(
                            rearSuspension =
                                it.rearSuspension?.copy(
                                    tokens = tokens
                                )
                        )
                    }
                }

                is BikeDetailsContract.Intent.Input.RearSuspensionTravel -> {
                    val travel = input.travel.toDoubleOrNull() ?: 0.0
                    bikeFlow.update {
                        it.copy(
                            rearSuspension =
                                it.rearSuspension?.copy(
                                    travel = Distance(travel)
                                )
                        )
                    }
                }
            }
        }

    private fun emitNewState(
        bike: Bike,
        editMode: Boolean = true,
        measurementUnit: MeasurementUnitType
    ) = if (editMode) {
        val validationResult = validateBikeUseCase(bikeFlow.value).getOrNull()
        BikeDetailsContract.State.Edit(
            bikeName = bike.name,
            bikeType = bike.type,
            validationFailure = validationResult,
            frontTirePressure = bike.frontTire.pressure.getAsUnit(measurementUnit).toString(),
            frontTireWidth = bike.frontTire.width.getAsUnit(measurementUnit).toString(),
            frontInternalRimWidth = bike.frontTire.internalRimWidthInMM?.asMetric()?.toString(),
            frontSuspensionTokens = bike.frontSuspension?.tokens?.toString(),
            frontSuspensionTravel = bike.frontSuspension?.travel?.getAsUnit(measurementUnit)?.toString(),
            frontSuspensionPressure = bike.frontSuspension?.pressure?.getAsUnit(measurementUnit)?.toString(),
            rearTirePressure = bike.rearTire.pressure.getAsUnit(measurementUnit).toString(),
            rearTireWidth = bike.rearTire.width.getAsUnit(measurementUnit).toString(),
            rearInternalRimWidth = bike.rearTire.internalRimWidthInMM?.asMetric()?.toString(),
            rearSuspensionTokens = bike.rearSuspension?.tokens?.toString(),
            rearSuspensionTravel = bike.rearSuspension?.travel?.getAsUnit(measurementUnit)?.toString(),
            rearSuspensionPressure = bike.rearSuspension?.pressure?.getAsUnit(measurementUnit)?.toString(),
            bike = bike
        )
    } else {
        BikeDetailsContract.State.Details(
            bike,
            measurementUnit
        )
    }
}

interface BikeDetailsContract : UnidirectionalViewModel<BikeDetailsContract.State, BikeDetailsContract.Intent, BikeDetailsContract.Event> {
    @Immutable
    sealed class State(val bike: Bike, val measurementUnitType: MeasurementUnitType) {
        open class Details(bike: Bike, measurementUnitType: MeasurementUnitType = MeasurementUnitType.METRIC) :
            State(bike, measurementUnitType)

        class Edit(
            bike: Bike,
            measurementUnitType: MeasurementUnitType = MeasurementUnitType.METRIC,
            val bikeName: String = bike.name,
            val bikeType: BikeType = bike.type,
            val frontTirePressure: String = bike.frontTire.pressure.getAsUnit(measurementUnitType).toString(),
            val frontTireWidth: String = bike.frontTire.width.getAsUnit(measurementUnitType).toString(),
            val frontInternalRimWidth: String? = bike.frontTire.internalRimWidthInMM?.asMetric()?.toString(),
            val frontSuspensionTravel: String? = bike.frontSuspension?.travel?.getAsUnit(measurementUnitType)?.toString(),
            val frontSuspensionPressure: String? = bike.frontSuspension?.pressure?.getAsUnit(measurementUnitType)?.toString(),
            val frontSuspensionTokens: String? = bike.frontSuspension?.tokens?.toString(),
            val rearTirePressure: String = bike.rearTire.pressure.getAsUnit(measurementUnitType).toString(),
            val rearTireWidth: String = bike.rearTire.width.getAsUnit(measurementUnitType).toString(),
            val rearInternalRimWidth: String? = bike.rearTire.internalRimWidthInMM?.asMetric()?.toString(),
            val rearSuspensionTravel: String? = bike.rearSuspension?.travel?.getAsUnit(measurementUnitType)?.toString(),
            val rearSuspensionPressure: String? = bike.rearSuspension?.pressure?.getAsUnit(measurementUnitType)?.toString(),
            val rearSuspensionTokens: String? = bike.rearSuspension?.tokens?.toString(),
            val validationFailure: ValidateBikeUseCase.DetailsFailure? = null
        ) : State(bike, measurementUnitType)
    }

    sealed class Event {
        data class ShowSnackbar(
            @StringRes val message: Int
        ) : Event()
    }

    sealed class Intent {
        data object OnEditClicked : Intent()

        data object OnBackPressed : Intent()

        data object OnSaveClicked : Intent()

        sealed class Input : Intent() {
            class BikeName(
                val name: String
            ) : Input()

            class BikeType(
                val type: com.alpenraum.shimstack.model.bike.BikeType
            ) : Input()

            class FrontTirePressure(
                val pressure: String
            ) : Input()

            class FrontTireWidth(
                val width: String
            ) : Input()

            class FrontTireInternalRimWidth(
                val width: String
            ) : Input()

            class RearTirePressure(
                val pressure: String
            ) : Input()

            class RearTireWidth(
                val width: String
            ) : Input()

            class RearTireInternalRimWidth(
                val width: String
            ) : Input()

            class FrontSuspensionTravel(
                val travel: String
            ) : Input()

            class RearSuspensionTravel(
                val travel: String
            ) : Input()

            class FrontSuspensionPressure(
                val pressure: String
            ) : Input()

            class RearSuspensionPressure(
                val pressure: String
            ) : Input()

            class FrontSuspensionTokens(
                val tokens: String
            ) : Input()

            class RearSuspensionTokens(
                val tokens: String
            ) : Input()
        }
    }
}