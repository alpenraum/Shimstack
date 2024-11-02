package com.alpenraum.shimstack.usersettingsdomain

import com.alpenraum.shimstack.model.measurementunit.MeasurementUnitType

data class UserSettings(
    val isDynamicColorEnabled: Boolean,
    val measurementUnitType: MeasurementUnitType,
    val isAnalyticsEnabled: Boolean,
    val isOnboardingCompleted: Boolean
)