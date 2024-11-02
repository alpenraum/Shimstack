package com.alpenraum.shimstack.home.settings

import androidx.annotation.StringRes
import androidx.navigation.NavController
import com.alpenraum.shimstack.home.R
import com.alpenraum.shimstack.model.measurementunit.MeasurementUnitType
import com.alpenraum.shimstack.ui.base.BaseViewModel
import com.alpenraum.shimstack.ui.base.UnidirectionalViewModel
import com.alpenraum.shimstack.usersettingsdomain.GetUserSettingsUseCase
import com.alpenraum.shimstack.usersettingsdomain.UserSettings
import com.alpenraum.shimstack.usersettingsdomain.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject constructor(
    private val getUserSettingsUseCase: GetUserSettingsUseCase,
    private val userSettingsRepository: UserSettingsRepository,
    dispatchersProvider: com.alpenraum.shimstack.common.DispatchersProvider
) : BaseViewModel(dispatchersProvider), SettingsContract {
    private val _event = MutableSharedFlow<SettingsContract.Event>()
    override val state: StateFlow<SettingsContract.State> = getUserSettingsUseCase().map(::mapUserSettings).map(::createState)
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = SettingsContract.State())
    override val event: SharedFlow<SettingsContract.Event>
        get() = _event.asSharedFlow()

    override fun intent(
        intent: SettingsContract.Intent, navController: NavController
    ) {
        when (intent) {
            is SettingsContract.Intent.OnAllowAnalyticsChange -> iOScope.launch {
                userSettingsRepository.updateIsAnalyticsEnabled(
                    intent.newSetting
                )
            }

            is SettingsContract.Intent.OnMeasurementUnitTypeChange -> iOScope.launch {
                userSettingsRepository.updateMeasurementUnitType(
                    intent.newSetting
                )
            }

            is SettingsContract.Intent.OnUseDynamicThemeChange -> iOScope.launch {
                userSettingsRepository.updateIsDynamicColorEnabled(
                    intent.newSetting
                )
            }
        }
    }

    private fun mapUserSettings(userSetting: UserSettings): List<SettingsContract.Settings> = listOf(
        SettingsContract.Settings.DynamicTheme(userSetting.isDynamicColorEnabled),
        SettingsContract.Settings.AllowAnalytics(userSetting.isAnalyticsEnabled),
        SettingsContract.Settings.MeasurementUnit(userSetting.measurementUnitType),
    )

    private fun createState(list: List<SettingsContract.Settings>): SettingsContract.State = SettingsContract.State(list)


}

interface SettingsContract : UnidirectionalViewModel<SettingsContract.State, SettingsContract.Intent, SettingsContract.Event> {
    data class State(val settings: List<Settings> = emptyList())

    sealed class Event

    sealed class Intent {
        class OnUseDynamicThemeChange(val newSetting: Boolean) : Intent()
        class OnAllowAnalyticsChange(val newSetting: Boolean) : Intent()
        class OnMeasurementUnitTypeChange(val newSetting: MeasurementUnitType) : Intent()
    }

    sealed class Settings(
        @StringRes val label: Int,

        ) {
        class DynamicTheme(val setting: Boolean) : Settings(R.string.settings_dynamic_theme)
        class AllowAnalytics(val setting: Boolean) : Settings(R.string.settings_allow_analytics)
        class MeasurementUnit(val options: List<Int>, val selectedIndex: Int) : Settings(R.string.settings_measurement_unit_type)
    }
}