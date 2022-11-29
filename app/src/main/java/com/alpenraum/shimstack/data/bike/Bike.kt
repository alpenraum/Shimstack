package com.alpenraum.shimstack.data.bike

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bike(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    @Embedded(prefix = "front_suspension_") val frontSuspension: Suspension,
    @Embedded(prefix = "rear_suspension_") val rearSuspension: Suspension? = null,
    @Embedded(prefix = "front_tire_") val frontTire: Tire,
    @Embedded(prefix = "rear_tire_") val rearTire: Tire
)
