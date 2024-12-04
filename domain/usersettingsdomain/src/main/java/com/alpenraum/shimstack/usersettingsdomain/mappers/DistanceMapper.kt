package com.alpenraum.shimstack.usersettingsdomain.mappers

import com.alpenraum.shimstack.model.measurementunit.Distance
import com.alpenraum.shimstack.model.measurementunit.MeasurementUnitType
import com.alpenraum.shimstack.usersettingsdomain.UserSettingsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DistanceMapper
@Inject
constructor(
    private val userSettingsRepository: UserSettingsRepository
) {
    suspend fun fromUserInput(input: Double): Distance {
        val unitType = userSettingsRepository.getUserSettings().first().measurementUnitType
        return when (unitType) {
            MeasurementUnitType.METRIC -> Distance(input)
            MeasurementUnitType.IMPERIAL -> Distance.fromImperial(input)
        }
    }

    // data layer exclusively uses metric units
    fun fromData(input: Double): Distance {
        return Distance(input)
    }
}