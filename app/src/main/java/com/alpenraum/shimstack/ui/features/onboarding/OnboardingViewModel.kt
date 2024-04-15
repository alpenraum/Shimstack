package com.alpenraum.shimstack.ui.features.onboarding

import androidx.lifecycle.viewModelScope
import com.alpenraum.shimstack.common.stores.ShimstackDataStore
import com.alpenraum.shimstack.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel
    @Inject
    constructor() : BaseViewModel() {
        @Suppress("ktlint:standard:backing-property-naming")
        private val _event = MutableSharedFlow<Event>()

        fun event() = _event.asSharedFlow()

        sealed class Event {
            data object NavigateToHomeScreen : Event()
        }

        override fun onStart() {
            super.onStart()
            viewModelScope.launch {
                ShimstackDataStore.isOnboardingCompleted?.collectLatest {
                    if (it) _event.emit(Event.NavigateToHomeScreen)
                }
            }
        }

        fun onSkipClicked() {
            viewModelScope.launch {
                ShimstackDataStore.setIsOnboardingCompleted(true)
                _event.emit(Event.NavigateToHomeScreen)
            }
        }
    }