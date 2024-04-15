package com.alpenraum.shimstack.data.models.tire

import android.content.Context
import com.alpenraum.shimstack.R
import com.alpenraum.shimstack.data.models.pressure.Pressure
import java.math.BigDecimal

data class Tire(
    val pressure: Pressure,
    val widthInMM: Double,
    val internalRimWidthInMM: Double?
) {
    private val widthInInches: Double
        get() = widthInMM / 2.54

    constructor() : this(Pressure(BigDecimal.ZERO), 0.0, 0.0)

    fun getFormattedPressure(context: Context) = pressure.toFormattedString(context)

    fun getFormattedTireWidth(context: Context) =
        if (true) { // TODO
            "$widthInMM ${context.getString(R.string.mm)}"
        } else {
            "$widthInInches${context.getString(R.string.inch)}"
        }

    fun getFormattedInternalRimWidth(context: Context) = "$internalRimWidthInMM ${context.getString(R.string.mm)}"
}