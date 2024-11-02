package com.alpenraum.shimstack.usersettingsdomain

import com.alpenraum.shimstack.model.measurementunit.MeasurementUnitType
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {

    fun getUserSettings(): Flow<UserSettings>

    suspend fun updateIsDynamicColorEnabled(enabled: Boolean)
    suspend fun updateMeasurementUnitType(type: MeasurementUnitType)
    suspend fun updateIsAnalyticsEnabled(enabled: Boolean)
    suspend fun updateIsOnboardingCompleted(enabled: Boolean)

}