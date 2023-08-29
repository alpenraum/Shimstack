package com.alpenraum.shimstack.data.bike

import android.content.Context
import androidx.room.Embedded
import androidx.room.Ignore
import java.math.BigDecimal

data class Tire(
    @Embedded(prefix = "pressure_") val pressure: Pressure,
    val widthInMM: Double,
    val internalRimWidthInMM: Double
) {
    @Ignore
    constructor() : this(Pressure(BigDecimal.ZERO), 0.0, 0.0)

    override fun toString(): String {
        return super.toString()
    }

    fun getFormattedPressure(context: Context) = pressure.toFormattedString(context)
}
