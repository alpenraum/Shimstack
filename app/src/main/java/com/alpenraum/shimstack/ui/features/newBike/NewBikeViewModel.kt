package com.alpenraum.shimstack.ui.features.newBike

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import com.alpenraum.shimstack.data.bike.Bike
import com.alpenraum.shimstack.data.bike.BikeDTO
import com.alpenraum.shimstack.data.bikeTemplates.BikeTemplate
import com.alpenraum.shimstack.data.bikeTemplates.LocalBikeTemplateRepository
import com.alpenraum.shimstack.ui.base.BaseViewModel
import com.alpenraum.shimstack.ui.base.UnidirectionalViewModel
import com.alpenraum.shimstack.ui.features.navArgs
import com.alpenraum.shimstack.usecases.biometrics.ValidateBikeDTOUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch

@HiltViewModel
class NewBikeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bikeTemplateRepository: LocalBikeTemplateRepository,
    private val validateBikeDTOUseCase: ValidateBikeDTOUseCase
) : BaseViewModel(), NewBikeContract {

    private val navArgs: NewBikeNavArgs = savedStateHandle.navArgs() // todo: remove

    private val _state = MutableStateFlow(NewBikeContract.State())
    override val state: StateFlow<NewBikeContract.State>
        get() = _state.asStateFlow()

    private val filterFlow = MutableStateFlow("")
    private var filterJob: Job? = null

    private val _event = MutableSharedFlow<NewBikeContract.Event>()
    override val event: SharedFlow<NewBikeContract.Event>
        get() = _event.asSharedFlow()

    init {
        iOScope.launch {
            _state.emit(
                NewBikeContract.State(
                    bikeTemplateRepository.getBikeTemplatesFilteredByName(
                        ""
                    ).toImmutableList()
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()
        iOScope.launch {
            filterJob = launchFilterBikeTemplates()
        }
    }

    override fun onStop() {
        super.onStop()
        filterJob?.cancel()
    }

    override fun intent(intent: NewBikeContract.Intent) {
        iOScope.launch {
            when (intent) {
                is NewBikeContract.Intent.Filter -> {
                    filterFlow.emit(intent.filter)
                }

// Details Data Input
                is NewBikeContract.Intent.BikeTemplateSelected -> goToEnterDetailsScreen(
                    intent.bike
                )

                is NewBikeContract.Intent.BikeNameInput -> validateAndUpdateInput(
                    DetailsInputData(name = intent.name)
                )

                is NewBikeContract.Intent.BikeTypeInput -> validateAndUpdateInput(
                    bikeType = intent.type
                )

                is NewBikeContract.Intent.EbikeInput -> validateAndUpdateInput(
                    isEbike = intent.isEbike
                )

                is NewBikeContract.Intent.FrontInternalRimWidthInput -> {
                    validateAndUpdateInput(
                        DetailsInputData(frontInternalRimWidth = intent.width)
                    )
                }

                is NewBikeContract.Intent.FrontSuspensionInput -> {
                    validateAndUpdateInput(
                        DetailsInputData(intent.travel)
                    )
                }

                is NewBikeContract.Intent.FrontTireWidthInput -> {
                    validateAndUpdateInput(
                        DetailsInputData(frontTireWidth = intent.width)
                    )
                }

                is NewBikeContract.Intent.RearInternalRimWidthInput -> {
                    validateAndUpdateInput(
                        DetailsInputData(rearInternalRimWidth = intent.width)
                    )
                }

                is NewBikeContract.Intent.RearSuspensionInput -> {
                    validateAndUpdateInput(
                        DetailsInputData(intent.travel)
                    )
                }

                is NewBikeContract.Intent.RearTireWidthInput -> {
                    validateAndUpdateInput(
                        DetailsInputData(rearTireWidth = intent.width)
                    )
                }

                NewBikeContract.Intent.OnNextClicked -> _event.emit(
                    NewBikeContract.Event.NavigateToNextStep
                )

                is NewBikeContract.Intent.FrontTirePressureInput -> validateAndUpdateInput(
                    setupInputData = SetupInputData(frontTirePressure = intent.pressure)
                )

                is NewBikeContract.Intent.RearTirePressureInput -> validateAndUpdateInput(
                    setupInputData = SetupInputData(rearTirePressure = intent.pressure)
                )

                is NewBikeContract.Intent.HSCInput -> {
                    if (intent.isFork) {
                        validateAndUpdateInput(hasHSCFork = intent.hasHsc)
                    } else {
                        validateAndUpdateInput(hasHSCShock = intent.hasHsc)
                    }
                }

                is NewBikeContract.Intent.HSRInput -> {
                    if (intent.isFork) {
                        validateAndUpdateInput(hasHSRFork = intent.hasHsr)
                    } else {
                        validateAndUpdateInput(hasHSRShock = intent.hasHsr)
                    }
                }

                is NewBikeContract.Intent.FrontSuspensionPressure -> validateAndUpdateInput(
                    setupInputData = SetupInputData(frontSuspensionPressure = intent.pressure)
                )

                is NewBikeContract.Intent.FrontSuspensionTokens -> validateAndUpdateInput(
                    setupInputData = SetupInputData(frontSuspensionTokens = intent.tokens)
                )

                is NewBikeContract.Intent.RearSuspensionPressure -> validateAndUpdateInput(
                    setupInputData = SetupInputData(rearSuspensionPressure = intent.pressure)
                )

                is NewBikeContract.Intent.RearSuspensionTokens -> validateAndUpdateInput(
                    setupInputData = SetupInputData(rearSuspensionTokens = intent.tokens)
                )

                is NewBikeContract.Intent.FrontSuspensionHSC -> validateAndUpdateInput(
                    setupInputData = SetupInputData(frontSuspensionHSC = intent.clicks)
                )

                is NewBikeContract.Intent.FrontSuspensionHSR -> validateAndUpdateInput(
                    setupInputData = SetupInputData(frontSuspensionHSR = intent.clicks)
                )

                is NewBikeContract.Intent.FrontSuspensionLSC -> validateAndUpdateInput(
                    setupInputData = SetupInputData(frontSuspensionLSC = intent.clicks)
                )

                is NewBikeContract.Intent.FrontSuspensionLSR -> validateAndUpdateInput(
                    setupInputData = SetupInputData(frontSuspensionHSR = intent.clicks)
                )

                is NewBikeContract.Intent.RearSuspensionHSC -> validateAndUpdateInput(
                    setupInputData = SetupInputData(rearSuspensionHSC = intent.clicks)
                )

                is NewBikeContract.Intent.RearSuspensionHSR -> validateAndUpdateInput(
                    setupInputData = SetupInputData(rearSuspensionHSR = intent.clicks)
                )

                is NewBikeContract.Intent.RearSuspensionLSC -> validateAndUpdateInput(
                    setupInputData = SetupInputData(rearSuspensionLSC = intent.clicks)
                )

                is NewBikeContract.Intent.RearSuspensionLSR -> validateAndUpdateInput(
                    setupInputData = SetupInputData(rearSuspensionLSR = intent.clicks)
                )
            }
        }
    }

    private suspend fun validateAndUpdateInput(
        detailsInputData: DetailsInputData? = null,
        setupInputData: SetupInputData? = null,
        isEbike: Boolean? = null,
        bikeType: Bike.Type? = null,
        hasHSCFork: Boolean? = null,
        hasHSRFork: Boolean? = null,
        hasHSCShock: Boolean? = null,
        hasHSRShock: Boolean? = null
    ) {
        iOScope.launch {
            val detailsInput = detailsInputData?.let { createNewInputData(it) }
            val setupInput = setupInputData?.let { createNewInputSetup(it) }
            // val validationResult = validateBikeDTOUseCase(newData) TODO
            _state.emit(
                state.value.copy(
                    detailsInput = detailsInput ?: state.value.detailsInput,
                    setupInput = setupInput ?: state.value.setupInput,
                    isEbike = isEbike ?: state.value.isEbike,
                    bikeType = bikeType ?: state.value.bikeType,
                    hasHSCFork = hasHSCFork ?: state.value.hasHSCFork,
                    hasHSRFork = hasHSRFork ?: state.value.hasHSRFork,
                    hasHSCShock = hasHSCShock ?: state.value.hasHSCShock,
                    hasHSRShock = hasHSRShock ?: state.value.hasHSRShock
//                    validationErrors = if (validationResult.isSuccess().not()) {
//                        validationResult as ValidateBikeDTOUseCase.Result.Failure
//                    } else {
//                        null
//                    }
                )
            )
        }
    }

    @OptIn(FlowPreview::class)
    private fun launchFilterBikeTemplates() = iOScope.launch {
        filterFlow.sample(200.milliseconds).distinctUntilChanged().collectLatest {
            val filteredTemplates = bikeTemplateRepository.getBikeTemplatesFilteredByName(it)
            _state.emit(state.value.copy(bikeTemplates = filteredTemplates.toImmutableList()))
        }
    }

    private fun goToEnterDetailsScreen(template: BikeTemplate?) = iOScope.launch {
        _state.emit(
            state.value.copy(
                detailsInput = mapFromBikeDTO(template?.toBikeDTO() ?: BikeDTO.empty())
            )
        )
        _event.emit(NewBikeContract.Event.NavigateToNextStep)
    }

    private fun createNewInputData(detailsInputData: DetailsInputData) = DetailsInputData(
        detailsInputData.name ?: state.value.detailsInput.name,
        detailsInputData.frontTravel ?: state.value.detailsInput.frontTravel,
        detailsInputData.rearTravel ?: state.value.detailsInput.rearTravel,
        detailsInputData.frontTireWidth ?: state.value.detailsInput.frontTireWidth,
        detailsInputData.rearTireWidth ?: state.value.detailsInput.rearTireWidth,
        detailsInputData.frontInternalRimWidth ?: state.value.detailsInput.frontInternalRimWidth,
        detailsInputData.rearInternalRimWidth ?: state.value.detailsInput.rearInternalRimWidth
    )

    private fun createNewInputSetup(setupInput: SetupInputData) = SetupInputData(
        setupInput.frontTirePressure ?: state.value.setupInput.frontTirePressure,
        setupInput.rearTirePressure ?: state.value.setupInput.rearTirePressure

    )

    private fun mapFromBikeDTO(bikeDTO: BikeDTO) = DetailsInputData(
        bikeDTO.name,
        bikeDTO.frontSuspension?.travel?.toString(),
        bikeDTO.frontSuspension?.travel?.toString(),
        bikeDTO.frontTire.widthInMM.toString(),
        bikeDTO.rearTire.widthInMM.toString(),
        bikeDTO.frontTire.internalRimWidthInMM?.toString(),
        bikeDTO.rearTire.internalRimWidthInMM?.toString()
    )
}

interface NewBikeContract :
    UnidirectionalViewModel<NewBikeContract.State, NewBikeContract.Intent, NewBikeContract.Event> {
    @Immutable
    data class State(
        val bikeTemplates: ImmutableList<BikeTemplate> = persistentListOf(),
        val validationErrors: ValidateBikeDTOUseCase.Result.Failure? = null,
        val isEbike: Boolean = false,
        val bikeType: Bike.Type = Bike.Type.UNKNOWN,
        val detailsInput: DetailsInputData = DetailsInputData(),
        val setupInput: SetupInputData = SetupInputData(),
        val hasHSCFork: Boolean = false,
        val hasHSRFork: Boolean = false,
        val hasHSCShock: Boolean = false,
        val hasHSRShock: Boolean = false
    ) {
        companion object {
            const val INPUT_NAME = "INPUT_NAME"
            const val INPUT_FRONT_TRAVEL = "INPUT_FRONT_TRAVEL"
            const val INPUT_REAR_TRAVEL = "INPUT_REAR_TRAVEL"
            const val INPUT_FRONT_TIRE_WIDTH = "INPUT_FRONT_TIRE_WIDTH"
            const val INPUT_REAR_TIRE_WIDTH = "INPUT_REAR_TIRE_WIDTH"
            const val INPUT_FRONT_INTERNAL_RIM_WIDTH = "INPUT_FRONT_INTERNAL_RIM_WIDTH"
            const val INPUT_REAR_INTERNAL_RIM_WIDTH = "INPUT_REAR_INTERNAL_RIM_WIDTH"

            // Setup
            const val INPUT_FRONT_TIRE_PRESSURE = "INPUT_FRONT_TIRE_PRESSURE"
            const val INPUT_REAR_TIRE_PRESSURE = "INPUT_REAR_TIRE_PRESSURE"
            const val INPUT_FRONT_SUSPENSION_PRESSURE = "INPUT_FRONT_SUSPENSION_PRESSURE"
        }
    }

    sealed class Event {
        data object NavigateToNextStep : Event()
        data object NavigateToPreviousStep : Event()
    }

    sealed class Intent {
        class Filter(val filter: String) : Intent()
        class BikeTemplateSelected(val bike: BikeTemplate) : Intent()

        object OnNextClicked : Intent()
        sealed class DataInput : Intent()
        class BikeNameInput(val name: String) : DataInput()
        class BikeTypeInput(val type: Bike.Type) : DataInput()
        class EbikeInput(val isEbike: Boolean) : DataInput()
        class HSCInput(val hasHsc: Boolean, val isFork: Boolean) : DataInput()
        class HSRInput(val hasHsr: Boolean, val isFork: Boolean) : DataInput()
        class FrontSuspensionInput(val travel: String?) : DataInput()
        class RearSuspensionInput(val travel: String?) : DataInput()

        class FrontTireWidthInput(val width: String) : DataInput()
        class RearTireWidthInput(val width: String) : DataInput()

        class FrontInternalRimWidthInput(val width: String?) : DataInput()
        class RearInternalRimWidthInput(val width: String?) : DataInput()

        sealed class SetupInput : Intent()

        class FrontTirePressureInput(val pressure: String?) : SetupInput()
        class RearTirePressureInput(val pressure: String?) : SetupInput()
        class FrontSuspensionPressure(val pressure: String?) : SetupInput()
        class FrontSuspensionTokens(val tokens: String?) : SetupInput()
        class RearSuspensionPressure(val pressure: String?) : SetupInput()
        class RearSuspensionTokens(val tokens: String?) : SetupInput()
        class FrontSuspensionLSC(val clicks: String?) : SetupInput()
        class FrontSuspensionHSC(val clicks: String?) : SetupInput()
        class FrontSuspensionLSR(val clicks: String?) : SetupInput()
        class FrontSuspensionHSR(val clicks: String?) : SetupInput()

        class RearSuspensionLSC(val clicks: String?) : SetupInput()
        class RearSuspensionHSC(val clicks: String?) : SetupInput()
        class RearSuspensionLSR(val clicks: String?) : SetupInput()
        class RearSuspensionHSR(val clicks: String?) : SetupInput()
    }
}

data class NewBikeNavArgs(
    val id: String
)

data class DetailsInputData(
    val name: String? = null,
    val frontTravel: String? = null,
    val rearTravel: String? = null,
    val frontTireWidth: String? = null,
    val rearTireWidth: String? = null,
    val frontInternalRimWidth: String? = null,
    val rearInternalRimWidth: String? = null
)

data class SetupInputData(
    val frontTirePressure: String? = null,
    val rearTirePressure: String? = null,
    val frontSuspensionPressure: String? = null,
    val rearSuspensionPressure: String? = null,
    val frontSuspensionTokens: String? = null,
    val rearSuspensionTokens: String? = null,
    val frontSuspensionLSC: String? = null,
    val frontSuspensionHSC: String? = null,
    val frontSuspensionLSR: String? = null,
    val frontSuspensionHSR: String? = null,
    val rearSuspensionLSC: String? = null,
    val rearSuspensionHSC: String? = null,
    val rearSuspensionLSR: String? = null,
    val rearSuspensionHSR: String? = null
)
