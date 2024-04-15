package com.alpenraum.shimstack.core.database.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DampingDTO(val lowSpeedFromClosed: Int, val highSpeedFromClosed: Int? = null) : Parcelable