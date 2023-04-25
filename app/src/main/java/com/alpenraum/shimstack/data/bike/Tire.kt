package com.alpenraum.shimstack.data.bike

data class Tire(val pressure: Double, val widthInMM: Double, val internalRimWidthInMM: Double) {
    constructor() : this(0.0, 0.0, 0.0)
}
