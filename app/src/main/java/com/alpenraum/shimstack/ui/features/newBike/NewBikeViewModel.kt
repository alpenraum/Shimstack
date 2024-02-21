package com.alpenraum.shimstack.ui.features.newBike

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import com.alpenraum.shimstack.R
import com.alpenraum.shimstack.data.bike.Bike
import com.alpenraum.shimstack.data.bike.BikeDTO
import com.alpenraum.shimstack.data.bike.Damping
import com.alpenraum.shimstack.data.bike.LocalBikeRepository
import com.alpenraum.shimstack.data.bike.Pressure
import com.alpenraum.shimstack.data.bike.Suspension
import com.alpenraum.shimstack.data.bike.Tire
import com.alpenraum.shimstack.data.bikeTemplates.BikeTemplate
import com.alpenraum.shimstack.data.bikeTemplates.LocalBikeTemplateRepository
import com.alpenraum.shimstack.ui.base.BaseViewModel
import com.alpenraum.shimstack.ui.base.UnidirectionalViewModel
import com.alpenraum.shimstack.usecases.ValidateBikeDTOUseCase
import com.alpenraum.shimstack.usecases.ValidateSetupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class NewBikeViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val bikeTemplateRepository: LocalBikeTemplateRepository,
    private val bikeRepository: LocalBikeRepository,
    private val validateBikeDTOUseCase: ValidateBikeDTOUseCase,
    private val validateSetupUseCase: ValidateSetupUseCase
) : BaseViewModel(), NewBikeContract {

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

                NewBikeContract.Intent.OnFlowFinished -> {
                    _event.emit(NewBikeContract.Event.NavigateToHomeScreen)
                }

// Details Data Input
                is NewBikeContract.Intent.BikeTemplateSelected ->
                    goToEnterDetailsScreen(
                        intent.bike
                    )

                is NewBikeContract.Intent.BikeNameInput ->
                    validateAndUpdateInput(
                        DetailsInputData(name = intent.name)
                    )

                is NewBikeContract.Intent.BikeTypeInput ->
                    validateAndUpdateInput(
                        bikeDTOType = intent.type
                    )

                is NewBikeContract.Intent.EbikeInput ->
                    validateAndUpdateInput(
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

                is NewBikeContract.Intent.OnNextClicked -> {
                    if (intent.flowFinished) {
                        try {
                            saveBike()
                            _event.emit(
                                NewBikeContract.Event.NavigateToNextStep
                            )
                        } catch (e: Exception) {
                            _event.emit(NewBikeContract.Event.ShowToast(R.string.error_create_bike))
                        }
                    } else {
                        _event.emit(
                            NewBikeContract.Event.NavigateToNextStep
                        )
                    }
                }

                is NewBikeContract.Intent.FrontTirePressureInput ->
                    validateAndUpdateInput(
                        setupInputData = SetupInputData(frontTirePressure = intent.pressure)
                    )

                is NewBikeContract.Intent.RearTirePressureInput ->
                    validateAndUpdateInput(
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

                is NewBikeContract.Intent.FrontSuspensionPressure ->
                    validateAndUpdateInput(
                        setupInputData =
                        SetupInputData(
                            frontSuspensionPressure = intent.pressure
                        )
                    )

                is NewBikeContract.Intent.FrontSuspensionTokens ->
                    validateAndUpdateInput(
                        setupInputData = SetupInputData(frontSuspensionTokens = intent.tokens)
                    )

                is NewBikeContract.Intent.RearSuspensionPressure ->
                    validateAndUpdateInput(
                        setupInputData =
                        SetupInputData(
                            rearSuspensionPressure = intent.pressure
                        )
                    )

                is NewBikeContract.Intent.RearSuspensionTokens ->
                    validateAndUpdateInput(
                        setupInputData = SetupInputData(rearSuspensionTokens = intent.tokens)
                    )

                is NewBikeContract.Intent.FrontSuspensionHSC ->
                    validateAndUpdateInput(
                        setupInputData = SetupInputData(frontSuspensionHSC = intent.clicks)
                    )

                is NewBikeContract.Intent.FrontSuspensionHSR ->
                    validateAndUpdateInput(
                        setupInputData = SetupInputData(frontSuspensionHSR = intent.clicks)
                    )

                is NewBikeContract.Intent.FrontSuspensionLSC ->
                    validateAndUpdateInput(
                        setupInputData = SetupInputData(frontSuspensionLSC = intent.clicks)
                    )

                is NewBikeContract.Intent.FrontSuspensionLSR ->
                    validateAndUpdateInput(
                        setupInputData = SetupInputData(frontSuspensionLSR = intent.clicks)
                    )

                is NewBikeContract.Intent.RearSuspensionHSC ->
                    validateAndUpdateInput(
                        setupInputData = SetupInputData(rearSuspensionHSC = intent.clicks)
                    )

                is NewBikeContract.Intent.RearSuspensionHSR ->
                    validateAndUpdateInput(
                        setupInputData = SetupInputData(rearSuspensionHSR = intent.clicks)
                    )

                is NewBikeContract.Intent.RearSuspensionLSC ->
                    validateAndUpdateInput(
                        setupInputData = SetupInputData(rearSuspensionLSC = intent.clicks)
                    )

                is NewBikeContract.Intent.RearSuspensionLSR ->
                    validateAndUpdateInput(
                        setupInputData = SetupInputData(rearSuspensionLSR = intent.clicks)
                    )
            }
        }
    }

    private suspend fun saveBike() {
        bikeRepository.createBike(_state.value.toBike())
    }

    private suspend fun validateAndUpdateInput(
        detailsInputData: DetailsInputData? = null,
        setupInputData: SetupInputData? = null,
        isEbike: Boolean? = null,
        bikeDTOType: BikeDTO.Type? = null,
        hasHSCFork: Boolean? = null,
        hasHSRFork: Boolean? = null,
        hasHSCShock: Boolean? = null,
        hasHSRShock: Boolean? = null
    ) {
        iOScope.launch {
            val detailsInput = detailsInputData?.let { createNewInputData(it) }
            val setupInput = setupInputData?.let { createNewInputSetup(it) }
            val detailsValidationResult =
                detailsInput?.let {
                    validateBikeDTOUseCase(
                        it,
                        bikeDTOType ?: state.value.bikeDTOType
                    )
                }
            val setupValidationResult = setupInput?.let { validateSetupUseCase(it) }
            _state.emit(
                state.value.copy(
                    detailsInput = detailsInput ?: state.value.detailsInput,
                    setupInput = setupInput ?: state.value.setupInput,
                    isEbike = isEbike ?: state.value.isEbike,
                    bikeDTOType = bikeDTOType ?: state.value.bikeDTOType,
                    hasHSCFork = hasHSCFork ?: state.value.hasHSCFork,
                    hasHSRFork = hasHSRFork ?: state.value.hasHSRFork,
                    hasHSCShock = hasHSCShock ?: state.value.hasHSCShock,
                    hasHSRShock = hasHSRShock ?: state.value.hasHSRShock,
                    detailsValidationErrors =
                    if (detailsValidationResult?.isSuccess()
                            ?.not() == true
                    ) {
                        detailsValidationResult as ValidateBikeDTOUseCase.DetailsFailure
                    } else {
                        null
                    },
                    setupValidationErrors =
                    if (setupValidationResult?.isSuccess()?.not() == true) {
                        setupValidationResult as ValidateSetupUseCase.SetupFailure
                    } else {
                        null
                    },
                    showSetupOutlierHint = setupValidationResult is ValidateSetupUseCase.SetupOutlier
                )
            )
        }
    }

    @OptIn(FlowPreview::class)
    private fun launchFilterBikeTemplates() =
        iOScope.launch {
            filterFlow.sample(200.milliseconds).distinctUntilChanged().collectLatest {
                val filteredTemplates =
                    bikeTemplateRepository.getBikeTemplatesFilteredByName(
                        it
                    )
                _state.emit(
                    state.value.copy(bikeTemplates = filteredTemplates.toImmutableList())
                )
            }
        }

    private fun goToEnterDetailsScreen(template: BikeTemplate?) =
        iOScope.launch {
            validateAndUpdateInput(
                mapFromBikeDTO(template?.toBike() ?: Bike.empty())
            )
            _event.emit(NewBikeContract.Event.NavigateToNextStep)
        }

    private fun createNewInputData(detailsInputData: DetailsInputData) =
        DetailsInputData(
            detailsInputData.name ?: state.value.detailsInput.name,
            detailsInputData.frontTravel ?: state.value.detailsInput.frontTravel,
            detailsInputData.rearTravel ?: state.value.detailsInput.rearTravel,
            detailsInputData.frontTireWidth ?: state.value.detailsInput.frontTireWidth,
            detailsInputData.rearTireWidth ?: state.value.detailsInput.rearTireWidth,
            detailsInputData.frontInternalRimWidth
                ?: state.value.detailsInput.frontInternalRimWidth,
            detailsInputData.rearInternalRimWidth ?: state.value.detailsInput.rearInternalRimWidth
        )

    private fun createNewInputSetup(setupInput: SetupInputData) =
        SetupInputData(
            setupInput.frontTirePressure ?: state.value.setupInput.frontTirePressure,
            setupInput.rearTirePressure ?: state.value.setupInput.rearTirePressure,
            frontSuspensionPressure = setupInput.frontSuspensionPressure
                ?: state.value.setupInput.frontSuspensionPressure,
            rearSuspensionPressure = setupInput.rearSuspensionPressure
                ?: state.value.setupInput.rearSuspensionPressure,
            frontSuspensionTokens = setupInput.frontSuspensionTokens
                ?: state.value.setupInput.frontSuspensionTokens,
            rearSuspensionTokens = setupInput.rearSuspensionTokens
                ?: state.value.setupInput.rearSuspensionTokens,
            frontSuspensionLSC = setupInput.frontSuspensionLSC
                ?: state.value.setupInput.frontSuspensionLSC,
            frontSuspensionLSR = setupInput.frontSuspensionLSR
                ?: state.value.setupInput.frontSuspensionLSR,
            frontSuspensionHSC = setupInput.frontSuspensionHSC
                ?: state.value.setupInput.frontSuspensionHSC,
            frontSuspensionHSR = setupInput.frontSuspensionHSR
                ?: state.value.setupInput.frontSuspensionHSR,
            rearSuspensionLSC = setupInput.rearSuspensionLSC
                ?: state.value.setupInput.rearSuspensionLSC,
            rearSuspensionLSR = setupInput.rearSuspensionLSR
                ?: state.value.setupInput.rearSuspensionLSR,
            rearSuspensionHSC = setupInput.rearSuspensionHSC
                ?: state.value.setupInput.rearSuspensionHSC,
            rearSuspensionHSR = setupInput.rearSuspensionHSR
                ?: state.value.setupInput.rearSuspensionHSR

        )

    private fun mapFromBikeDTO(bike: Bike) =
        DetailsInputData(
            bike.name,
            bike.frontSuspension?.travel?.toString(),
            bike.frontSuspension?.travel?.toString(),
            bike.frontTire.widthInMM.toString(),
            bike.rearTire.widthInMM.toString(),
            bike.frontTire.internalRimWidthInMM?.toString(),
            bike.rearTire.internalRimWidthInMM?.toString()
        )
}

interface NewBikeContract :
    UnidirectionalViewModel<NewBikeContract.State, NewBikeContract.Intent, NewBikeContract.Event> {
    @Immutable
    data class State(
        val bikeTemplates: ImmutableList<BikeTemplate> = persistentListOf(),
        val detailsValidationErrors: ValidateBikeDTOUseCase.DetailsFailure? = null,
        val setupValidationErrors: ValidateSetupUseCase.SetupFailure? = null,
        val showSetupOutlierHint: Boolean = false,
        val isEbike: Boolean = false,
        val bikeDTOType: BikeDTO.Type = BikeDTO.Type.UNKNOWN,
        val detailsInput: DetailsInputData = DetailsInputData(),
        val setupInput: SetupInputData = SetupInputData(),
        val hasHSCFork: Boolean = false,
        val hasHSRFork: Boolean = false,
        val hasHSCShock: Boolean = false,
        val hasHSRShock: Boolean = false
    ) {

        fun hasFrontSuspension() = detailsInput.frontTravel?.isNotEmpty() == true
        fun hasRearSuspension() = detailsInput.rearTravel?.isNotEmpty() == true
        fun toBike(): BikeDTO {
            val frontSuspension = if (hasFrontSuspension()) {
                Suspension(
                    Pressure(setupInput.frontSuspensionPressure?.toDouble() ?: 0.0),
                    Damping(
                        setupInput.frontSuspensionLSC?.toInt() ?: 0,
                        if (hasHSCFork) setupInput.frontSuspensionHSC?.toInt() ?: 0 else null
                    ),
                    Damping(
                        setupInput.frontSuspensionLSR?.toInt() ?: 0,
                        if (hasHSRFork) setupInput.frontSuspensionHSR?.toInt() ?: 0 else null
                    ),
                    detailsInput.frontTravel?.toInt() ?: 0,
                    setupInput.frontSuspensionTokens?.toInt() ?: 0
                )
            } else {
                null
            }
            val rearSuspension = if (hasRearSuspension()) {
                Suspension(
                    Pressure(setupInput.rearSuspensionPressure?.toDouble() ?: 0.0),
                    Damping(
                        setupInput.rearSuspensionLSC?.toInt() ?: 0,
                        if (hasHSCShock) setupInput.rearSuspensionHSC?.toInt() ?: 0 else null
                    ),
                    Damping(
                        setupInput.rearSuspensionLSR?.toInt() ?: 0,
                        if (hasHSRShock) setupInput.rearSuspensionHSR?.toInt() ?: 0 else null
                    ),
                    detailsInput.rearTravel?.toInt() ?: 0,
                    setupInput.rearSuspensionTokens?.toInt() ?: 0
                )
            } else {
                null
            }
            val frontTire = Tire(
                Pressure(setupInput.frontTirePressure?.toDouble() ?: 0.0),
                detailsInput.frontTireWidth?.toDouble() ?: 0.0,
                detailsInput.frontInternalRimWidth?.toDoubleOrNull()
            )
            val rearTire = Tire(
                Pressure(setupInput.rearTirePressure?.toDouble() ?: 0.0),
                detailsInput.rearTireWidth?.toDouble() ?: 0.0,
                detailsInput.rearInternalRimWidth?.toDoubleOrNull()
            )

            return BikeDTO(
                name = detailsInput.name ?: "",
                type = bikeDTOType,
                isEBike = isEbike,
                frontSuspension = frontSuspension,
                rearSuspension = rearSuspension,
                frontTire = frontTire,
                rearTire = rearTire
            )
        }
    }

    sealed class Event {
        data object NavigateToNextStep : Event()

        data object NavigateToPreviousStep : Event()

        data object NavigateToHomeScreen : Event()

        class ShowToast(@StringRes val messageRes: Int) : Event()
    }

    sealed class Intent {
        class Filter(val filter: String) : Intent()

        class BikeTemplateSelected(val bike: BikeTemplate) : Intent()

        class OnNextClicked(val flowFinished: Boolean = false) : Intent()

        sealed class DataInput : Intent()

        class BikeNameInput(val name: String) : DataInput()

        class BikeTypeInput(val type: BikeDTO.Type) : DataInput()

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

        data object OnFlowFinished : Intent()
    }
}

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