package com.alpenraum.shimstack.ui.features.newBike

import androidx.compose.runtime.Immutable
import com.alpenraum.shimstack.data.bikeTemplates.BikeTemplate
import com.alpenraum.shimstack.data.bikeTemplates.LocalBikeTemplateRepository
import com.alpenraum.shimstack.ui.base.BaseViewModel
import com.alpenraum.shimstack.ui.base.UnidirectionalViewModel
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
    private val bikeTemplateRepository: LocalBikeTemplateRepository
) : BaseViewModel(), NewBikeContract {

    private val _state = MutableStateFlow(NewBikeContract.State.Entry())
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

        // filterJob = launchFilterBikeTemplates()
    }

    override fun onStop() {
        super.onStop()
        //   filterJob?.cancel()
    }

    override fun intent(intent: NewBikeContract.Intent) {
        when (intent) {
            is NewBikeContract.Intent.Filter -> {
                iOScope.launch {
                    val filteredTemplates =
                        bikeTemplateRepository.getBikeTemplatesFilteredByName(intent.filter)
                    _state.emit(NewBikeContract.State.Entry(filteredTemplates.toImmutableList()))
                }
            } // filterFlow.value = intent.filter
        }
    }

    @OptIn(FlowPreview::class)
    private fun launchFilterBikeTemplates() = iOScope.launch {
        filterFlow.sample(200.milliseconds).distinctUntilChanged().collectLatest {
            val filteredTemplates = bikeTemplateRepository.getBikeTemplatesFilteredByName(it)
            _state.emit(NewBikeContract.State.Entry(filteredTemplates.toImmutableList()))
        }
    }
}

interface NewBikeContract :
    UnidirectionalViewModel<NewBikeContract.State, NewBikeContract.Intent, NewBikeContract.Event> {
    @Immutable
    sealed class State {
        data class Entry(val bikeTemplates: ImmutableList<BikeTemplate> = persistentListOf()) :
            State()
    }

    sealed class Event

    sealed class Intent {
        class Filter(val filter: String) : Intent()
    }
}
