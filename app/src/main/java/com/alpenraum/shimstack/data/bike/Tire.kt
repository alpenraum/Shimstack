package com.alpenraum.shimstack.data.bike

import android.content.Context
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Ignore
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class Tire(
    @Embedded(prefix = "pressure_") val pressure: Pressure,
    val widthInMM: Double,
    val internalRimWidthInMM: Double?
) : Parcelable {
    @Ignore
    constructor() : this(Pressure(BigDecimal.ZERO), 0.0, 0.0)

    fun getFormattedPressure(context: Context) = pressure.toFormattedString(context)
}