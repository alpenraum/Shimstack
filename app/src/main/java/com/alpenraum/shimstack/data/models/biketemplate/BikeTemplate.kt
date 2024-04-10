package com.alpenraum.shimstack.data.models.biketemplate

import com.alpenraum.shimstack.data.models.bike.Bike
import com.alpenraum.shimstack.data.models.bike.BikeType
import com.alpenraum.shimstack.data.models.pressure.Pressure
import com.alpenraum.shimstack.data.models.suspension.Damping
import com.alpenraum.shimstack.data.models.suspension.Suspension
import com.alpenraum.shimstack.data.models.tire.Tire

data class BikeTemplate(
    val id: Int?,
    val name: String,
    val type: BikeType,
    val isEBike: Boolean,
    val frontSuspensionTravelInMM: Int,
    val rearSuspensionTravelInMM: Int,
    val frontTireWidthInMM: Double,
    val frontRimWidthInMM: Double,
    val rearTireWidthInMM: Double,
    val rearRimWidthInMM: Double
) {
    companion object {
        fun testData() =
            BikeTemplate(
                id = 0,
                name = "Evil Offering V2",
                type = BikeType.ALL_MTN,
                isEBike = false,
                frontSuspensionTravelInMM = 140,
                rearSuspensionTravelInMM = 140,
                frontRimWidthInMM = 30.0,
                frontTireWidthInMM = 50.0,
                rearRimWidthInMM = 30.0,
                rearTireWidthInMM = 45.0
            )
    }

    fun toDTO() =
        BikeTemplateDTO(
            id,
            name,
            type,
            isEBike,
            frontSuspensionTravelInMM,
            rearSuspensionTravelInMM,
            frontTireWidthInMM,
            frontRimWidthInMM,
            rearTireWidthInMM
        )

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
}