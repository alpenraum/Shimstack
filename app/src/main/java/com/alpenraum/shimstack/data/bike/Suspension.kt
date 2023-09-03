package com.alpenraum.shimstack.data.bike

import androidx.room.Embedded

data class Suspension(
    @Embedded(prefix = "pressure_") val pressure: Pressure,
    @Embedded(prefix = "compression_") val compression: Damping,
    @Embedded(prefix = "rebound_") val rebound: Damping,
    val tokens: Int
)

data class Damping(val lowSpeedFromClosed: Int, val highSpeedFromClosed: Int? = null)
