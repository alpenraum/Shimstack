package com.alpenraum.shimstack.core.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alpenraum.shimstack.core.database.db.AppDatabase
import com.squareup.moshi.JsonClass
import javax.annotation.concurrent.Immutable

@JsonClass(generateAdapter = true)
@Entity(tableName = AppDatabase.TABLE_BIKE_TEMPLATE)
@Immutable
class BikeTemplateDTO(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val type: Int,
    val isEBike: Boolean,
    val frontSuspensionTravelInMM: Int,
    val rearSuspensionTravelInMM: Int,
    val frontTireWidthInMM: Double = 0.0,
    val frontRimWidthInMM: Double = 0.0,
    val rearTireWidthInMM: Double = 0.0,
    val rearRimWidthInMM: Double = 0.0
)