package com.alpenraum.shimstack.model.tire

import com.alpenraum.shimstack.model.pressure.Pressure
import java.math.BigDecimal

data class Tire(
    val pressure: Pressure,
    val widthInMM: Double,
    val internalRimWidthInMM: Double?
) {
    val widthInInches: Double
        get() = widthInMM / 2.54

    constructor() : this(Pressure(BigDecimal.ZERO), 0.0, 0.0)
}