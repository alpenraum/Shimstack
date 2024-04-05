package com.alpenraum.shimstack.data.bike

import android.content.Context
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Ignore
import com.alpenraum.shimstack.R
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class Tire(
    @Embedded(prefix = "pressure_") val pressure: Pressure,
    val widthInMM: Double,
    val internalRimWidthInMM: Double?
) : Parcelable {
    private val widthInInches: Double
        get() = widthInMM / 2.54

    @Ignore
    constructor() : this(Pressure(BigDecimal.ZERO), 0.0, 0.0)

    fun getFormattedPressure(context: Context) = pressure.toFormattedString(context)

    fun getFormattedTireWidth(context: Context) =
        if (true) { // TODO
            "$widthInMM ${context.getString(R.string.mm)}"
        } else {
            "$widthInInches${context.getString(R.string.inch)}"
        }

    fun getFormattedInternalRimWidth(context: Context) =
        "$internalRimWidthInMM ${context.getString(R.string.mm)}"
}