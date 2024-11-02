package com.alpenraum.shimstack.model.measurementunit

import java.math.BigDecimal
import java.math.RoundingMode

data class Distance(private val distanceInMM: BigDecimal) : MeasurementUnit {
    override fun asMetric(): BigDecimal {
        return distanceInMM
    }

    override fun asImperial(): BigDecimal {
        return distanceInMM.times(MM_TO_INCH_CONVERSION).setScale(
            1,
            RoundingMode.HALF_EVEN
        )
    }

    constructor(distanceInMM: Double) : this(BigDecimal(distanceInMM))

    override val storageKey = "PREF_DISTANCE_UNIT"

    companion object {
        val MM_TO_INCH_CONVERSION = BigDecimal(3.0 / 64.0)
    }
}