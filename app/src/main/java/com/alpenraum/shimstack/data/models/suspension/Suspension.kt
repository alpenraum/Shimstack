package com.alpenraum.shimstack.data.models.suspension

import android.content.Context
import androidx.compose.runtime.Immutable
import com.alpenraum.shimstack.R
import com.alpenraum.shimstack.data.models.pressure.Pressure

@Immutable
data class Suspension(
    val pressure: Pressure,
    val compression: Damping,
    val rebound: Damping,
    val tokens: Int,
    val travel: Int
) {
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

@Immutable
data class Damping(val lowSpeedFromClosed: Int, val highSpeedFromClosed: Int? = null)