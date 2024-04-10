package com.alpenraum.shimstack.data.models.biketemplate

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alpenraum.shimstack.data.db.AppDatabase
import com.alpenraum.shimstack.data.models.bike.BikeType
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = AppDatabase.TABLE_BIKE_TEMPLATE)
@Immutable
class BikeTemplateDTO(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val type: BikeType,
    val isEBike: Boolean,
    val frontSuspensionTravelInMM: Int,
    val rearSuspensionTravelInMM: Int,
    val frontTireWidthInMM: Double = 0.0,
    val frontRimWidthInMM: Double = 0.0,
    val rearTireWidthInMM: Double = 0.0,
    val rearRimWidthInMM: Double = 0.0
) {
    fun toDomain() =
        BikeTemplate(
            id,
            name,
            type,
            isEBike,
            frontSuspensionTravelInMM,
            rearSuspensionTravelInMM,
            frontTireWidthInMM,
            frontRimWidthInMM,
            rearTireWidthInMM,
            rearRimWidthInMM
        )
}