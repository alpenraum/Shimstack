package com.alpenraum.shimstack.data.bike

import androidx.room.Ignore

data class Tire(val pressure: Double, val widthInMM: Double, val internalRimWidthInMM: Double) {
    @Ignore
    constructor() : this(0.0, 0.0, 0.0)
}
