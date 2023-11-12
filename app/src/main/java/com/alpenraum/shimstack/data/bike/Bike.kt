package com.alpenraum.shimstack.data.bike

import android.content.Context
import androidx.annotation.StringRes
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alpenraum.shimstack.R
import com.alpenraum.shimstack.data.db.AppDatabase
import com.alpenraum.shimstack.ui.features.mainScreens.home.UIDataLabel
import java.math.BigDecimal

@Entity(tableName = AppDatabase.table_bike)
data class Bike(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val type: Type,
    @Embedded(prefix = "front_suspension_") val frontSuspension: Suspension? = null,
    @Embedded(prefix = "rear_suspension_") val rearSuspension: Suspension? = null,
    @Embedded(prefix = "front_tire_") val frontTire: Tire,
    @Embedded(prefix = "rear_tire_") val rearTire: Tire,
    val isEBike: Boolean
) {

    fun toDTO() = BikeDTO(name, type, frontSuspension, rearSuspension, frontTire, rearTire, isEBike)

    companion object {
        fun empty() =
            Bike(
                name = "",
                type = Type.UNKNOWN,
                isEBike = false,
                frontTire = Tire(Pressure(BigDecimal.ZERO), 0.0, 0.0),
                rearTire = Tire(Pressure(BigDecimal.ZERO), 0.0, 0.0)
            )
    }

    enum class Type(@StringRes val labelRes: Int) {
        ROAD(R.string.label_road_type),
        GRAVEL(R.string.label_gravel_type),
        XC(R.string.label_xc_type),
        TRAIL(R.string.label_trail_type),
        ALL_MTN(R.string.label_all_mtn_type),
        ENDURO(R.string.label_enduro_type),
        DH(R.string.label_dh_type),
        UNKNOWN(R.string.label_unknown_type)
    }
}

data class BikeDTO(
    val name: String,
    val type: Bike.Type,
    val frontSuspension: Suspension? = null,
    val rearSuspension: Suspension? = null,
    val frontTire: Tire,
    val rearTire: Tire,
    val isEBike: Boolean
) {

    companion object {
        fun empty() =
            BikeDTO(
                name = "",
                type = Bike.Type.UNKNOWN,
                isEBike = false,
                frontTire = Tire(Pressure(BigDecimal.ZERO), 0.0, 0.0),
                rearTire = Tire(Pressure(BigDecimal.ZERO), 0.0, 0.0),
                frontSuspension = Suspension(Pressure(0.0), Damping(0), Damping(0), 0),
                rearSuspension = Suspension(Pressure(0.0), Damping(0), Damping(0), 0)
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

    fun getFrontSuspensionUIData(context: Context): List<UIDataLabel>? = frontSuspension?.let {
        return getSuspensionUIData(it, context)
    }

    fun getRearSuspensionUIData(context: Context): List<UIDataLabel>? = rearSuspension?.let {
        return getSuspensionUIData(it, context)
    }

    private fun getSuspensionUIData(suspension: Suspension, context: Context): List<UIDataLabel> {
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

            return uiData
        }
    }

    // TODO: Create DTOs FOR ALL SUBDATA
    private fun getDampingUIData(damping: Damping, isRebound: Boolean, context: Context) = if (damping.highSpeedFromClosed != null) {
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
}
