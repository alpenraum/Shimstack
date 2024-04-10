package com.alpenraum.shimstack.data.models.bike

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alpenraum.shimstack.data.db.AppDatabase
import com.alpenraum.shimstack.data.models.pressure.Pressure
import com.alpenraum.shimstack.data.models.suspension.Suspension
import com.alpenraum.shimstack.data.models.tire.Tire
import java.math.BigDecimal

@Entity(tableName = AppDatabase.TABLE_BIKE)
data class BikeDTO(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val type: BikeType,
    @Embedded(prefix = "front_suspension_") val frontSuspension: Suspension? = null,
    @Embedded(prefix = "rear_suspension_") val rearSuspension: Suspension? = null,
    @Embedded(prefix = "front_tire_") val frontTire: Tire,
    @Embedded(prefix = "rear_tire_") val rearTire: Tire,
    val isEBike: Boolean
) {
    companion object {
        fun empty() =
            BikeDTO(
                name = "",
                type = BikeType.UNKNOWN,
                isEBike = false,
                frontTire = Tire(Pressure(BigDecimal.ZERO), 0.0, 0.0),
                rearTire = Tire(Pressure(BigDecimal.ZERO), 0.0, 0.0)
            )
    }
}