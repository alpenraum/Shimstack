package com.alpenraum.shimstack.model.bike

import com.alpenraum.shimstack.model.measurementunit.Distance
import com.alpenraum.shimstack.model.measurementunit.Pressure
import com.alpenraum.shimstack.model.suspension.Suspension
import com.alpenraum.shimstack.model.tire.Tire
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
        fun empty() =
            Bike(
                name = "",
                type = BikeType.UNKNOWN,
                isEBike = false,
                frontTire = Tire(Pressure(BigDecimal.ZERO), Distance(BigDecimal.ZERO), Distance(BigDecimal.ZERO)),
                rearTire = Tire(Pressure(BigDecimal.ZERO), Distance(BigDecimal.ZERO), Distance(BigDecimal.ZERO)),
                frontSuspension = null,
                rearSuspension = null,
                id = 0
            )
    }


}