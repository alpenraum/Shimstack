package com.alpenraum.shimstack.data.bike

import androidx.compose.runtime.Immutable
import androidx.room.Embedded

@Immutable
data class Suspension(
    @Embedded(prefix = "pressure_") val pressure: Pressure,
    @Embedded(prefix = "compression_") val compression: Damping,
    @Embedded(prefix = "rebound_") val rebound: Damping,
    val tokens: Int,
    val travel: Int
) {
    constructor(travel: Int) : this(Pressure(0.0), Damping(0), Damping(0), 0, travel)
}

@Immutable
data class Damping(val lowSpeedFromClosed: Int, val highSpeedFromClosed: Int? = null)
