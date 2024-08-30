package com.alpenraum.shimstack.model.suspension

import com.alpenraum.shimstack.model.pressure.Pressure

data class Suspension(
    val pressure: Pressure,
    val compression: Damping,
    val rebound: Damping,
    val tokens: Int,
    val travel: Int
) {
    constructor(travel: Int) : this(Pressure(0.0), Damping(0), Damping(0), 0, travel)
}

data class Damping(val lowSpeedFromClosed: Int, val highSpeedFromClosed: Int? = null)