package com.alpenraum.shimstack.data.bike

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity
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

    enum class Type {
        ROAD,
        GRAVEL,
        XC,
        TRAIL,
        ALL_MTN,
        ENDURO,
        DH,
        UNKNOWN
    }
}
