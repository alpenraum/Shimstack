package com.alpenraum.shimstack.model.measurementunit

enum class MeasurementUnitType {
    METRIC,
    IMPERIAL;

    fun isMetric() = this == METRIC
}