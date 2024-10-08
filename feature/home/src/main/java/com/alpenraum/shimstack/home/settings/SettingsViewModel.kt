package com.alpenraum.shimstack.home.settings

import androidx.annotation.StringRes
import androidx.navigation.NavController
import com.alpenraum.shimstack.datastore.ShimstackDatastore
import com.alpenraum.shimstack.home.R
import com.alpenraum.shimstack.ui.base.BaseViewModel
import com.alpenraum.shimstack.ui.base.UnidirectionalViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        private val datastore: ShimstackDatastore,
        dispatchersProvider: com.alpenraum.shimstack.common.DispatchersProvider
    ) : BaseViewModel(dispatchersProvider), SettingsContract {
        private val _state = MutableStateFlow(SettingsContract.State())
        private val _event = MutableSharedFlow<SettingsContract.Event>()
        override val state: StateFlow<SettingsContract.State>
            get() = _state.asStateFlow()
        override val event: SharedFlow<SettingsContract.Event>
            get() = _event.asSharedFlow()

        override fun intent(
            intent: SettingsContract.Intent,
            navController: NavController
        ) {
            when (intent) {
                is SettingsContract.Intent.OnSettingsChanged ->
                    toggleSetting(
                        intent.settings,
                        intent.newSetting
                    )
            }
        }

        override fun onStart() {
            super.onStart()
            viewModelScope.launch {
                val state =
                    SettingsContract.State(
                        listOf(
                            Pair(
                                SettingsContract.Settings.USE_DYNAMIC_THEME,
                                datastore.useDynamicTheme
                            ),
                            Pair(SettingsContract.Settings.ALLOW_ANALYTICS, datastore.allowAnalytics)
                        )
                    )
                _state.emit(state)
            }
        }

        private fun toggleSetting(
            settings: SettingsContract.Settings,
            newSetting: Boolean
        ) = iOScope.launch {
            when (settings) {
                SettingsContract.Settings.USE_DYNAMIC_THEME ->
                    datastore.setUseDynamicTheme(
                        newSetting
                    )

                SettingsContract.Settings.ALLOW_ANALYTICS -> datastore.setAllowAnalytics(newSetting)
            }
        }
    }

interface SettingsContract :
    UnidirectionalViewModel<SettingsContract.State, SettingsContract.Intent, SettingsContract.Event> {
    data class State(val settings: List<Pair<Settings, Flow<Boolean>?>> = emptyList())

    sealed class Event

    sealed class Intent(val newSetting: Boolean) {
        class OnSettingsChanged(val settings: Settings, newSetting: Boolean) : Intent(newSetting)
    }

    enum class Settings(
        @StringRes val label: Int
    ) {
        USE_DYNAMIC_THEME(R.string.settings_dynamic_theme),
        ALLOW_ANALYTICS(R.string.settings_allow_analytics)
    }
}