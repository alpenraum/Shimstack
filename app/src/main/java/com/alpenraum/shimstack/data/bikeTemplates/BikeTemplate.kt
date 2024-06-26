package com.alpenraum.shimstack.data.bikeTemplates

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alpenraum.shimstack.data.bike.Bike
import com.alpenraum.shimstack.data.bike.BikeDTO
import com.alpenraum.shimstack.data.bike.Damping
import com.alpenraum.shimstack.data.bike.Pressure
import com.alpenraum.shimstack.data.bike.Suspension
import com.alpenraum.shimstack.data.bike.Tire
import com.alpenraum.shimstack.data.db.AppDatabase
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = AppDatabase.TABLE_BIKE_TEMPLATE)
@Immutable
class BikeTemplate(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val type: BikeDTO.Type,
    val isEBike: Boolean,
    val frontSuspensionTravelInMM: Int,
    val rearSuspensionTravelInMM: Int,
    val frontTireWidthInMM: Double = 0.0,
    val frontRimWidthInMM: Double = 0.0,
    val rearTireWidthInMM: Double = 0.0,
    val rearRimWidthInMM: Double = 0.0
) {
    fun toBike() =
        Bike(
            name = name,
            type = type,
            frontSuspension =
            if (frontSuspensionTravelInMM == 0) {
                null
            } else {
                Suspension(
                    Pressure(0.0),
                    Damping(0, 0),
                    Damping(0, 0),
                    0,
                    frontSuspensionTravelInMM
                )
            },
            rearSuspension =
            if (rearSuspensionTravelInMM == 0) {
                null
            } else {
                Suspension(
                    Pressure(0.0),
                    Damping(0, 0),
                    Damping(0, 0),
                    0,
                    rearSuspensionTravelInMM
                )
            },
            frontTire = Tire(Pressure(0.0), frontTireWidthInMM, frontRimWidthInMM),
            rearTire =
            Tire(
                Pressure(0.0),
                rearTireWidthInMM,
                rearRimWidthInMM
            ),
            isEBike = isEBike,
            id = 0
        )

    companion object {
        fun testData() =
            BikeTemplate(
                id = 0,
                name = "Evil Offering V2",
                type = BikeDTO.Type.ALL_MTN,
                isEBike = false,
                frontSuspensionTravelInMM = 140,
                rearSuspensionTravelInMM = 140,
                frontRimWidthInMM = 30.0,
                frontTireWidthInMM = 50.0,
                rearRimWidthInMM = 30.0,
                rearTireWidthInMM = 45.0
            )
    }
}