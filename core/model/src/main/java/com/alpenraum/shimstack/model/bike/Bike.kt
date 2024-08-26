package com.alpenraum.shimstack.model.bike

import com.alpenraum.shimstack.model.pressure.Pressure
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
                frontTire = Tire(Pressure(BigDecimal.ZERO), 0.0, 0.0),
                rearTire = Tire(Pressure(BigDecimal.ZERO), 0.0, 0.0),
                frontSuspension = null,
                rearSuspension = null,
                id = 0
            )
    }

    fun isPopulated() = name.isNotBlank() && type != BikeType.UNKNOWN && frontTire.widthInMM != 0.0 && rearTire.widthInMM != 0.0

    fun hasSetup() =
        !frontTire.pressure.isEmpty() &&
            !rearTire.pressure.isEmpty() &&
            frontSuspension?.pressure?.isEmpty() != true &&
            rearSuspension?.pressure?.isEmpty() != true
}