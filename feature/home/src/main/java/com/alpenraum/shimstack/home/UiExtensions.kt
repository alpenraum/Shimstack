package com.alpenraum.shimstack.home

import android.content.Context
import com.alpenraum.shimstack.home.overview.UIDataLabel
import com.alpenraum.shimstack.model.bike.Bike
import com.alpenraum.shimstack.model.suspension.Damping
import com.alpenraum.shimstack.model.suspension.Suspension
import com.alpenraum.shimstack.ui.R
import com.alpenraum.shimstack.ui.compose.getFormattedPressure
import com.alpenraum.shimstack.ui.compose.toFormattedString
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

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
        UIDataLabel.Simple(context.getString(R.string.tokens), tokens.toString())

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