package com.alpenraum.shimstack.data

import com.alpenraum.shimstack.core.database.models.BikeDTO
import com.alpenraum.shimstack.core.database.models.BikeTemplateDTO
import com.alpenraum.shimstack.core.database.models.DampingDTO
import com.alpenraum.shimstack.core.database.models.SuspensionDTO
import com.alpenraum.shimstack.core.database.models.TireDTO
import com.alpenraum.shimstack.model.bike.Bike
import com.alpenraum.shimstack.model.bike.BikeType
import com.alpenraum.shimstack.model.biketemplate.BikeTemplate
import com.alpenraum.shimstack.model.pressure.Pressure
import com.alpenraum.shimstack.model.suspension.Damping
import com.alpenraum.shimstack.model.suspension.Suspension
import com.alpenraum.shimstack.model.tire.Tire

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

fun BikeTemplate.toDTO() =
    BikeTemplateDTO(
        id,
        name,
        type.id,
        isEBike,
        frontSuspensionTravelInMM,
        rearSuspensionTravelInMM,
        frontTireWidthInMM,
        frontRimWidthInMM,
        rearTireWidthInMM
    )

fun SuspensionDTO.toDomain() =
    Suspension(
        Pressure(pressure),
        compression.toDomain(),
        rebound.toDomain(),
        tokens,
        travel
    )

fun Suspension.toDTO() = SuspensionDTO(pressure.pressureInBar, compression.toDTO(), rebound.toDTO(), tokens, travel)

fun DampingDTO.toDomain() = Damping(lowSpeedFromClosed, highSpeedFromClosed)

fun Damping.toDTO() = DampingDTO(lowSpeedFromClosed, highSpeedFromClosed)

fun TireDTO.toDomain() = Tire(Pressure(pressure), widthInMM, internalRimWidthInMM)

fun Tire.toDTO() = TireDTO(pressure.pressureInBar, widthInMM, internalRimWidthInMM)

fun Bike.toDTO() =
    BikeDTO(id, name, type.id, frontSuspension?.toDTO(), rearSuspension?.toDTO(), frontTire.toDTO(), rearTire.toDTO(), isEBike)

fun BikeDTO.toDomain() =
    Bike(
        id ?: 0,
        name,
        BikeType.fromId(type),
        frontSuspension?.toDomain(),
        rearSuspension?.toDomain(),
        frontTire.toDomain(),
        rearTire.toDomain(),
        isEBike
    )