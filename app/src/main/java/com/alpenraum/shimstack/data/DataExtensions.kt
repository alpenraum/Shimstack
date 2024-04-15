package com.alpenraum.shimstack.data

import com.alpenraum.shimstack.core.database.models.BikeDTO
import com.alpenraum.shimstack.core.database.models.BikeTemplateDTO
import com.alpenraum.shimstack.core.database.models.DampingDTO
import com.alpenraum.shimstack.core.database.models.SuspensionDTO
import com.alpenraum.shimstack.core.database.models.TireDTO
import com.alpenraum.shimstack.data.models.bike.Bike
import com.alpenraum.shimstack.data.models.bike.BikeType
import com.alpenraum.shimstack.data.models.biketemplate.BikeTemplate
import com.alpenraum.shimstack.data.models.pressure.Pressure
import com.alpenraum.shimstack.data.models.suspension.Damping
import com.alpenraum.shimstack.data.models.suspension.Suspension
import com.alpenraum.shimstack.data.models.tire.Tire

fun BikeTemplateDTO.toDomain() =
    BikeTemplate(
        id,
        name,
        BikeType.fromId(type),
        isEBike,
        frontSuspensionTravelInMM,
        rearSuspensionTravelInMM,
        frontTireWidthInMM,
        frontRimWidthInMM,
        rearTireWidthInMM,
        rearRimWidthInMM
    )

fun SuspensionDTO.toDomain() = Suspension(Pressure(pressure), compression.toDomain(), rebound.toDomain(), tokens, travel)

fun Suspension.toDTO() = SuspensionDTO(pressure.pressureInBar, compression.toDTO(), rebound.toDTO(), tokens, travel)

fun DampingDTO.toDomain() = Damping(lowSpeedFromClosed, highSpeedFromClosed)

fun Damping.toDTO() = DampingDTO(lowSpeedFromClosed, highSpeedFromClosed)

fun TireDTO.toDomain() = Tire(Pressure(pressure), widthInMM, internalRimWidthInMM)

fun Tire.toDTO() = TireDTO(pressure.pressureInBar, widthInMM, internalRimWidthInMM)

fun Bike.toDTO() =
    BikeDTO(id, name, type.id, frontSuspension?.toDTO(), rearSuspension?.toDTO(), frontTire.toDTO(), rearTire.toDTO(), isEBike)