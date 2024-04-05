package com.alpenraum.shimstack.data.bike

import android.content.Context
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.room.Embedded
import com.alpenraum.shimstack.R
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
data class Suspension(
    @Embedded(prefix = "pressure_") val pressure: Pressure,
    @Embedded(prefix = "compression_") val compression: Damping,
    @Embedded(prefix = "rebound_") val rebound: Damping,
    val tokens: Int,
    val travel: Int
) : Parcelable {
    constructor(travel: Int) : this(Pressure(0.0), Damping(0), Damping(0), 0, travel)

    fun getFormattedPressure(context: Context) = pressure.toFormattedString(context)

    fun getFormattedTravel(context: Context) = "$travel ${context.getString(R.string.mm)}"

    fun getFormattedCompression(context: Context) =
        "${compression.lowSpeedFromClosed} ${context.getString(R.string.lsc)}${
            compression.highSpeedFromClosed?.let {
                " / $it ${
                    context.getString(R.string.hsc)
                }"
            } ?: ""
        }"

    fun getFormattedRebound(context: Context) =
        "${rebound.lowSpeedFromClosed} ${context.getString(R.string.lsr)}${
            rebound.highSpeedFromClosed?.let {
                " / $it ${
                    context.getString(R.string.hsr)
                }"
            } ?: ""
        }"
}

@Parcelize
@Immutable
data class Damping(val lowSpeedFromClosed: Int, val highSpeedFromClosed: Int? = null) : Parcelable