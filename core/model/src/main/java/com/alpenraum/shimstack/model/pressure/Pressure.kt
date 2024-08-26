package com.alpenraum.shimstack.model.pressure

import java.math.BigDecimal
import java.math.RoundingMode

data class Pressure(val pressureInBar: BigDecimal) {
    constructor(pressureInBar: Double) : this(pressureInBar.toBigDecimal())

    companion object {
        val BAR_TO_PSI_CONVERSION = BigDecimal(14.503773773)
    }

    val pressureInPSI: BigDecimal
        get() = getPressureInPsi()

    private fun getPressureInPsi() =
        (pressureInBar.times(BAR_TO_PSI_CONVERSION)).setScale(
            1,
            RoundingMode.HALF_EVEN
        )

    fun isEmpty() = pressureInBar != BigDecimal.ZERO
}