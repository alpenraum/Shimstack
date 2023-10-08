package com.alpenraum.shimstack.ui.screens.newBike

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

@HiltViewModel
class NewBikeViewModel @Inject constructor() : BaseViewModel(), NewBikeContract {

    private val _state = MutableStateFlow(NewBikeContract.State.Entry)
    override val state: StateFlow<NewBikeContract.State>
        get() = _state.asStateFlow()

    private val _event = MutableSharedFlow<NewBikeContract.Event>()
    override val event: SharedFlow<NewBikeContract.Event>
        get() = _event.asSharedFlow()

    override fun intent(intent: NewBikeContract.Intent) {
        when (intent) {
            else -> {}
        }
    }
}

interface NewBikeContract :
    UnidirectionalViewModel<NewBikeContract.State, NewBikeContract.Intent, NewBikeContract.Event> {
    sealed class State {
        object Entry : State()
    }

    sealed class Event

    sealed class Intent
}
