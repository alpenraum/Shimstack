package com.alpenraum.shimstack.ui.features.newBike

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import com.alpenraum.shimstack.data.bike.Bike
import com.alpenraum.shimstack.data.bike.BikeDTO
import com.alpenraum.shimstack.data.bike.Suspension
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

    private val _state = MutableStateFlow<NewBikeContract.State>(NewBikeContract.State.Entry())
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
                NewBikeContract.State.Entry(
                    bikeTemplateRepository.getBikeTemplatesFilteredByName(
                        ""
                    ).toImmutableList()
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()

        filterJob = launchFilterBikeTemplates()
    }

    override fun onStop() {
        super.onStop()
        filterJob?.cancel()
    }

    override fun intent(intent: NewBikeContract.Intent) {
        iOScope.launch {
            when (intent) {
                is NewBikeContract.Intent.Filter -> {
                    filterFlow.value = intent.filter
                }
// Navigation
                NewBikeContract.Intent.OnNextClicked -> {
                    when (_state.value) {
                        is NewBikeContract.State.Entry -> goToEnterDetailsScreen(null)
                        is NewBikeContract.State.Details -> { /* TODO */
                        }
                    }
                }

// Details Data Input
                is NewBikeContract.Intent.BikeTemplateSelected -> goToEnterDetailsScreen(
                    intent.bike
                )

                is NewBikeContract.Intent.BikeNameInput -> validateAndUpdateInput(
                    (_state.value as NewBikeContract.State.Details).bike.copy(
                        name = intent.name
                    )
                )

                is NewBikeContract.Intent.BikeTypeInput -> validateAndUpdateInput(
                    (_state.value as NewBikeContract.State.Details).bike.copy(
                        type = intent.type
                    )
                )

                is NewBikeContract.Intent.EbikeInput -> validateAndUpdateInput(
                    (_state.value as NewBikeContract.State.Details).bike.copy(
                        isEBike = intent.isEbike
                    )
                )

                is NewBikeContract.Intent.FrontInternalRimWidthInput -> {
                    val data = (_state.value as NewBikeContract.State.Details).bike
                    validateAndUpdateInput(
                        data.copy(
                            frontTire = data.frontTire.copy(internalRimWidthInMM = intent.width)
                        )
                    )
                }

                is NewBikeContract.Intent.FrontSuspensionInput -> {
                    val data = (_state.value as NewBikeContract.State.Details).bike
                    validateAndUpdateInput(
                        data.copy(
                            frontSuspension = if (intent.travel != null && intent.travel > 0) {
                                data.frontSuspension?.copy(
                                    travel = intent.travel
                                ) ?: Suspension(intent.travel)
                            } else {
                                null
                            }
                        )
                    )
                }

                is NewBikeContract.Intent.FrontTireWidthInput -> {
                    val data = (_state.value as NewBikeContract.State.Details).bike
                    validateAndUpdateInput(
                        data.copy(
                            frontTire = data.frontTire.copy(widthInMM = intent.width)
                        )
                    )
                }

                is NewBikeContract.Intent.RearInternalRimWidthInput -> {
                    val data = (_state.value as NewBikeContract.State.Details).bike
                    validateAndUpdateInput(
                        data.copy(
                            rearTire = data.rearTire.copy(internalRimWidthInMM = intent.width)
                        )
                    )
                }

                is NewBikeContract.Intent.RearSuspensionInput -> {
                    val data = (_state.value as NewBikeContract.State.Details).bike
                    validateAndUpdateInput(
                        data.copy(
                            frontSuspension = if (intent.travel != null && intent.travel > 0) {
                                data.frontSuspension?.copy(
                                    travel = intent.travel
                                ) ?: Suspension(intent.travel)
                            } else {
                                null
                            }
                        )
                    )
                }

                is NewBikeContract.Intent.RearTireWidthInput -> {
                    val data = (_state.value as NewBikeContract.State.Details).bike
                    validateAndUpdateInput(
                        data.copy(
                            rearTire = data.rearTire.copy(widthInMM = intent.width)
                        )
                    )
                }
            }
        }
    }

    private suspend fun validateAndUpdateInput(newData: BikeDTO) {
        iOScope.launch {
            when (_state.value) {
                is NewBikeContract.State.Details -> {
                    val validationResult = validateBikeDTOUseCase(newData)

                    _state.emit(
                        NewBikeContract.State.Details(
                            newData,
                            if (validationResult.isSuccess().not()) {
                                validationResult as ValidateBikeDTOUseCase.Result.Failure
                            } else {
                                null
                            }
                        )
                    )
                }

                else -> return@launch
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun launchFilterBikeTemplates() = iOScope.launch {
        filterFlow.sample(200.milliseconds).distinctUntilChanged().collectLatest {
            val filteredTemplates = bikeTemplateRepository.getBikeTemplatesFilteredByName(it)
            _state.emit(NewBikeContract.State.Entry(filteredTemplates.toImmutableList()))
        }
    }

    private fun goToEnterDetailsScreen(template: BikeTemplate?) = iOScope.launch {
        _state.emit(NewBikeContract.State.Details(template?.toBikeDTO() ?: BikeDTO.empty()))
    }
}

interface NewBikeContract :
    UnidirectionalViewModel<NewBikeContract.State, NewBikeContract.Intent, NewBikeContract.Event> {
    @Immutable
    sealed class State {
        data class Entry(val bikeTemplates: ImmutableList<BikeTemplate> = persistentListOf()) :
            State()

        data class Details(
            val bike: BikeDTO,
            val validationErrors: ValidateBikeDTOUseCase.Result.Failure? = null
        ) : State()
    }

    sealed class Event

    sealed class Intent {
        class Filter(val filter: String) : Intent()
        data object OnNextClicked : Intent()
        class BikeTemplateSelected(val bike: BikeTemplate) : Intent()

        sealed class DataInput : Intent()
        class BikeNameInput(val name: String) : DataInput()
        class BikeTypeInput(val type: Bike.Type) : DataInput()
        class EbikeInput(val isEbike: Boolean) : DataInput()
        class FrontSuspensionInput(val travel: Int?) : DataInput()
        class RearSuspensionInput(val travel: Int?) : DataInput()

        class FrontTireWidthInput(val width: Double) : DataInput()
        class RearTireWidthInput(val width: Double) : DataInput()

        class FrontInternalRimWidthInput(val width: Double?) : DataInput()
        class RearInternalRimWidthInput(val width: Double?) : DataInput()
    }
}

data class NewBikeNavArgs(
    val id: String
)
