package com.alpenraum.shimstack.core.database.models

import android.os.Parcelable
import androidx.room.Embedded
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class SuspensionDTO(
    val pressure: BigDecimal,
    @Embedded(prefix = "compression_") val compression: DampingDTO,
    @Embedded(prefix = "rebound_") val rebound: DampingDTO,
    val tokens: Int,
    val travel: Int
) : Parcelable