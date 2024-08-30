package com.alpenraum.shimstack.core.database.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alpenraum.shimstack.core.database.db.AppDatabase

@Entity(tableName = AppDatabase.TABLE_BIKE)
data class BikeDTO(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val type: Int,
    @Embedded(prefix = "front_suspension_") val frontSuspension: SuspensionDTO? = null,
    @Embedded(prefix = "rear_suspension_") val rearSuspension: SuspensionDTO? = null,
    @Embedded(prefix = "front_tire_") val frontTire: TireDTO,
    @Embedded(prefix = "rear_tire_") val rearTire: TireDTO,
    val isEBike: Boolean
)