package com.alpenraum.shimstack.model.measurementunit

import java.math.BigDecimal

interface MeasurementUnit {
    fun asMetric(): BigDecimal

    fun asImperial(): BigDecimal

    val storageKey: String

    fun getAsUnit(measurementUnitType: MeasurementUnitType) = if (measurementUnitType.isMetric()) this.asMetric() else this.asImperial()
}