package com.alpenraum.shimstack.data.models.bike

import android.content.Context
import com.alpenraum.shimstack.R
import com.alpenraum.shimstack.core.database.models.BikeDTO
import com.alpenraum.shimstack.data.models.pressure.Pressure
import com.alpenraum.shimstack.data.models.suspension.Damping
import com.alpenraum.shimstack.data.models.suspension.Suspension
import com.alpenraum.shimstack.data.models.tire.Tire
import com.alpenraum.shimstack.data.toDomain
import com.alpenraum.shimstack.ui.features.mainScreens.home.UIDataLabel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.math.BigDecimal

data class Bike(
    val id: Int? = null,
    val name: String,
    val type: BikeType,
    val frontSuspension: Suspension? = null,
    val rearSuspension: Suspension? = null,
    val frontTire: Tire,
    val rearTire: Tire,
    val isEBike: Boolean
) {
    companion object {
        fun fromDto(bikeDTO: BikeDTO) =
            with(bikeDTO) {
                Bike(
                    id ?: 0,
                    name,
                    BikeType.fromId(type),
                    frontSuspension?.toDomain(),
                    rearSuspension?.toDomain(),
                    frontTire.toDomain(),
                    rearTire.toDomain(),
                    isEBike
                )
            }

        fun empty() =
            Bike(
                name = "",
                type = BikeType.UNKNOWN,
                isEBike = false,
                frontTire = Tire(Pressure(BigDecimal.ZERO), 0.0, 0.0),
                rearTire = Tire(Pressure(BigDecimal.ZERO), 0.0, 0.0),
                frontSuspension = null,
                rearSuspension = null,
                id = 0
            )
    }

    fun getTireUIData(context: Context) =
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

    fun getFrontSuspensionUIData(context: Context): ImmutableList<UIDataLabel>? =
        frontSuspension?.let {
            return getSuspensionUIData(it, context).toImmutableList()
        }

    fun getRearSuspensionUIData(context: Context): ImmutableList<UIDataLabel>? =
        rearSuspension?.let {
            return getSuspensionUIData(it, context).toImmutableList()
        }

    private fun getSuspensionUIData(
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

    private fun getDampingUIData(
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

    fun isPopulated() = name.isNotBlank() && type != BikeType.UNKNOWN && frontTire.widthInMM != 0.0 && rearTire.widthInMM != 0.0

    fun hasSetup() =
        !frontTire.pressure.isEmpty() &&
            !rearTire.pressure.isEmpty() &&
            frontSuspension?.pressure?.isEmpty() != true &&
            rearSuspension?.pressure?.isEmpty() != true
}