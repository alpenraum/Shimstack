package com.alpenraum.shimstack.data

import com.alpenraum.shimstack.core.database.models.BikeDTO
import com.alpenraum.shimstack.core.database.models.BikeTemplateDTO
import com.alpenraum.shimstack.core.database.models.DampingDTO
import com.alpenraum.shimstack.core.database.models.SuspensionDTO
import com.alpenraum.shimstack.core.database.models.TireDTO
import com.alpenraum.shimstack.model.bike.Bike
import com.alpenraum.shimstack.model.bike.BikeType
import com.alpenraum.shimstack.model.biketemplate.BikeTemplate
import com.alpenraum.shimstack.model.measurementunit.Distance
import com.alpenraum.shimstack.model.measurementunit.Pressure
import com.alpenraum.shimstack.model.suspension.Damping
import com.alpenraum.shimstack.model.suspension.Suspension
import com.alpenraum.shimstack.model.tire.Tire
import java.math.BigDecimal

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
        Distance(BigDecimal(travel))
    )

fun Suspension.toDTO() = SuspensionDTO(pressure.asMetric(), compression.toDTO(), rebound.toDTO(), tokens, travel.asMetric().toInt())

fun DampingDTO.toDomain() = Damping(lowSpeedFromClosed, highSpeedFromClosed)

fun Damping.toDTO() = DampingDTO(lowSpeedFromClosed, highSpeedFromClosed)

fun TireDTO.toDomain() = Tire(Pressure(pressure), Distance(widthInMM), internalRimWidthInMM?.let { Distance(it) })

// TODO HAVE DB STORE BIG DECIMALS
fun Tire.toDTO() = TireDTO(pressure.asMetric(), width.asMetric().toDouble(), internalRimWidthInMM?.asMetric()?.toDouble())

fun Bike.toDTO() =
    BikeDTO(id, name, type.id, frontSuspension?.toDTO(), rearSuspension?.toDTO(), this.frontTire.toDTO(), this.rearTire.toDTO(), isEBike)

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