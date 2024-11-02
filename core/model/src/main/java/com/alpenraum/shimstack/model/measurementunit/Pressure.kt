package com.alpenraum.shimstack.model.measurementunit

import java.math.BigDecimal
import java.math.RoundingMode

data class Pressure(private val pressureInBar: BigDecimal) : MeasurementUnit {
    constructor(pressureInBar: Double) : this(pressureInBar.toBigDecimal())

    companion object {
        private val BAR_TO_PSI_CONVERSION = BigDecimal(14.503773773)
    }

    fun isEmpty() = pressureInBar != BigDecimal.ZERO

    override fun asMetric(): BigDecimal {
        return pressureInBar
    }

    override fun asImperial(): BigDecimal {
        return (pressureInBar.times(BAR_TO_PSI_CONVERSION)).setScale(
            1,
            RoundingMode.HALF_EVEN
        )
    }

    override val storageKey = "PREF_PRESSURE_UNIT"
}