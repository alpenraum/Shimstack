package com.alpenraum.shimstack.usersettingsdomain

import android.content.Context
import androidx.core.os.ConfigurationCompat
import com.alpenraum.shimstack.model.measurementunit.MeasurementUnitType
import javax.inject.Inject

class DecideUserMeasurementUnitUseCase
    @Inject
    constructor(private val userSettingsRepository: UserSettingsRepository) {
        suspend operator fun invoke(context: Context): MeasurementUnitType {
            val locale = ConfigurationCompat.getLocales(context.resources.configuration).get(0)?.country

            val unit =
                if (imperialRegions.any {
                        it.equals(
                            locale,
                            ignoreCase = true
                        )
                    }
                ) {
                    MeasurementUnitType.IMPERIAL
                } else {
                    MeasurementUnitType.METRIC
                }

            userSettingsRepository.updateMeasurementUnitType(unit)
            return unit
        }

        companion object {
            private val imperialRegions = listOf("us", "uk", "mm", "lr")
        }
    }