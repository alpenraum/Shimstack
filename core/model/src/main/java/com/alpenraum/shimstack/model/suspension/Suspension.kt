package com.alpenraum.shimstack.model.suspension

import com.alpenraum.shimstack.model.measurementunit.Distance
import com.alpenraum.shimstack.model.measurementunit.Pressure
import java.math.BigDecimal

data class Suspension(
    val pressure: Pressure,
    val compression: Damping,
    val rebound: Damping,
    val tokens: Int,
    val travel: Distance
) {
    constructor(travel: Int) : this(Pressure(0.0), Damping(0), Damping(0), 0, Distance(BigDecimal(travel)))
}

data class Damping(val lowSpeedFromClosed: Int, val highSpeedFromClosed: Int? = null)