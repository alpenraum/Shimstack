package com.alpenraum.shimstack.model.tire

import com.alpenraum.shimstack.model.measurementunit.Distance
import com.alpenraum.shimstack.model.measurementunit.Pressure
import java.math.BigDecimal

data class Tire(
    val pressure: Pressure,
    val width: Distance,
    val internalRimWidthInMM: Distance?
) {

    constructor() : this(Pressure(BigDecimal.ZERO), Distance(BigDecimal.ZERO), Distance(BigDecimal.ZERO))
}