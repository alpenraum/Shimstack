package com.alpenraum.shimstack.ui.compose

import android.content.Context
import com.alpenraum.shimstack.model.measurementunit.Pressure
import com.alpenraum.shimstack.model.suspension.Suspension
import com.alpenraum.shimstack.model.tire.Tire
import com.alpenraum.shimstack.ui.R

// TOOD: EXTRACT TO USE CASES
// region Tire
fun Tire.getFormattedPressure(
    context: Context,
    isMetric: Boolean
) = pressure.toFormattedString(context, isMetric)

fun Tire.getFormattedTireWidth(
    context: Context,
    isMetric: Boolean
) = if (isMetric) {
    "${width.asMetric()} ${context.getString(R.string.mm)}"
} else {
    "${width.asImperial()}${context.getString(R.string.inch)}"
}

fun Tire.getFormattedInternalRimWidth(context: Context) = "$internalRimWidthInMM ${context.getString(R.string.mm)}"

// endregion
// region Suspension
fun Suspension.getFormattedCompression(context: Context) =
    "${compression.lowSpeedFromClosed} ${context.getString(R.string.lsc)}${
        compression.highSpeedFromClosed?.let {
            " / $it ${
                context.getString(R.string.hsc)
            }"
        } ?: ""
    }"

fun Suspension.getFormattedRebound(context: Context) =
    "${rebound.lowSpeedFromClosed} ${context.getString(R.string.lsr)}${
        rebound.highSpeedFromClosed?.let {
            " / $it ${
                context.getString(R.string.hsr)
            }"
        } ?: ""
    }"

fun Suspension.getFormattedPressure(
    context: Context,
    isMetric: Boolean
) = pressure.toFormattedString(context, isMetric)

fun Suspension.getFormattedTravel(
    context: Context,
    isMetric: Boolean
) = if (isMetric) {
    "$travel ${context.getString(R.string.mm)}"
} else {
    "$travel ${context.getString(R.string.mm)}"
}

// endRegion

// Region Pressure

fun Pressure.toFormattedString(
    context: Context,
    isMetric: Boolean
): String =
    if (isMetric) {
        "${asMetric()} ${context.getString(R.string.bar)}"
    } else {
        "${asImperial()} ${context.getString(R.string.psi)}"
    }

// endRegion

// Region Bike