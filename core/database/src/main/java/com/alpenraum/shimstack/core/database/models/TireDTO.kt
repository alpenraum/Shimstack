package com.alpenraum.shimstack.core.database.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class TireDTO(
    val pressure: BigDecimal,
    val widthInMM: Double,
    val internalRimWidthInMM: Double?
) : Parcelable