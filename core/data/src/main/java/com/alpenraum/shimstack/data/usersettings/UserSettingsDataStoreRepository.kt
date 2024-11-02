package com.alpenraum.shimstack.data.usersettings

import com.alpenraum.shimstack.datastore.ShimstackDatastore
import com.alpenraum.shimstack.model.measurementunit.MeasurementUnitType
import com.alpenraum.shimstack.usersettingsdomain.UserSettings
import com.alpenraum.shimstack.usersettingsdomain.UserSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class UserSettingsDataStoreRepository @Inject constructor(private val dataStore: ShimstackDatastore) : UserSettingsRepository {
    override fun getUserSettings(): Flow<UserSettings> {
        return combine(
            dataStore.allowAnalytics,
            dataStore.useDynamicTheme,
            dataStore.measurementUnitType,
            dataStore.isOnboardingCompleted
        ) { x1, x2, x3, x4 ->
            val measurementUnitType = try {
                MeasurementUnitType.valueOf(x3)
            } catch (e: IllegalArgumentException) {
                MeasurementUnitType.METRIC
            }
            UserSettings(
                isDynamicColorEnabled = x2,
                isAnalyticsEnabled = x1,
                measurementUnitType = measurementUnitType,
                isOnboardingCompleted = x4
            )
        }
    }

    override suspend fun updateIsDynamicColorEnabled(enabled: Boolean) {
        dataStore.setUseDynamicTheme(enabled)
    }

    override suspend fun updateMeasurementUnitType(type: MeasurementUnitType) {
        dataStore.setMeasurementUnit(type.name)
    }

    override suspend fun updateIsAnalyticsEnabled(enabled: Boolean) {
        dataStore.setAllowAnalytics(enabled)
    }

    override suspend fun updateIsOnboardingCompleted(enabled: Boolean) {
        dataStore.setIsOnboardingCompleted(enabled)
    }
}