package com.alpenraum.shimstack.ui

import android.content.Context
import com.alpenraum.shimstack.R
import com.alpenraum.shimstack.model.bike.Bike
import com.alpenraum.shimstack.model.pressure.Pressure
import com.alpenraum.shimstack.model.suspension.Damping
import com.alpenraum.shimstack.model.suspension.Suspension
import com.alpenraum.shimstack.model.tire.Tire
import com.alpenraum.shimstack.ui.features.mainScreens.home.UIDataLabel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

// TOOD: EXTRACT TO USE CASES
// region Tire
fun Tire.getFormattedPressure(context: Context) = pressure.toFormattedString(context)

fun Tire.getFormattedTireWidth(context: Context) =
    if (true) { // TODO
        "$widthInMM ${context.getString(R.string.mm)}"
    } else {
        "$widthInInches${context.getString(R.string.inch)}"
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

fun Suspension.getFormattedPressure(context: Context) = pressure.toFormattedString(context)

fun Suspension.getFormattedTravel(context: Context) = "$travel ${context.getString(R.string.mm)}"

// endRegion

// Region Pressure

fun Pressure.toFormattedString(context: Context): String {
    return if (true) { // TODO
        "$pressureInBar ${context.getString(R.string.bar)}"
    } else {
        "$pressureInPSI ${context.getString(R.string.psi)}"
    }
}

// endRegion

// Region Bike

fun Bike.getTireUIData(context: Context) =
    Pair(
        UIDataLabel.Simple(
            context.getString(R.string.front),
            frontTire.getFormattedPressure(context)
        ),
        UIDataLabel.Simple(
            context.getString(R.string.rear),
            rearTire.getFormattedPressure(context)
        )
    )

fun Bike.getFrontSuspensionUIData(context: Context): ImmutableList<UIDataLabel>? =
    frontSuspension?.let {
        return getSuspensionUIData(it, context).toImmutableList()
    }

fun Bike.getRearSuspensionUIData(context: Context): ImmutableList<UIDataLabel>? =
    rearSuspension?.let {
        return getSuspensionUIData(it, context).toImmutableList()
    }

private fun Bike.getSuspensionUIData(
    suspension: Suspension,
    context: Context
): ImmutableList<UIDataLabel> {
    with(suspension) {
        val uiData = ArrayList<UIDataLabel>()

        val compressionData = getDampingUIData(compression, false, context)
        uiData.add(compressionData)
        val reboundData = getDampingUIData(rebound, true, context)
        uiData.add(reboundData)

        uiData.add(
            UIDataLabel.Simple(
                context.getString(R.string.pressure),
                pressure.toFormattedString(context)
            )
        )
        uiData.add(UIDataLabel.Simple(context.getString(R.string.tokens), tokens.toString()))

        return uiData.toImmutableList()
    }
}

private fun Bike.getDampingUIData(
    damping: Damping,
    isRebound: Boolean,
    context: Context
) = if (damping.highSpeedFromClosed != null) {
    UIDataLabel.Complex(
        mapOf(
            context.getString(if (isRebound) R.string.lsr else R.string.lsc) to damping.lowSpeedFromClosed.toString(),
            context.getString(if (isRebound) R.string.hsr else R.string.hsc) to damping.highSpeedFromClosed.toString()
        )
    )
} else {
    UIDataLabel.Simple(
        context.getString(if (isRebound) R.string.rebound else R.string.comp),
        damping.lowSpeedFromClosed.toString()
    )
}