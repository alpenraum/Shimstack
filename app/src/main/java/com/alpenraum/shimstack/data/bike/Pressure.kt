package com.alpenraum.shimstack.data.bike

import android.content.Context
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.room.Ignore
import com.alpenraum.shimstack.R
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.math.RoundingMode

@Parcelize
@Immutable
data class Pressure(val pressureInBar: BigDecimal) : Parcelable {
    @Ignore
    constructor(pressureInBar: Double) : this(pressureInBar.toBigDecimal())

    companion object {
        val BAR_TO_PSI_CONVERSION = BigDecimal(14.503773773)
    }

    fun toFormattedString(context: Context): String {
        return if (true) { // TODO
            "$pressureInBar ${context.getString(R.string.bar)}"
        } else {
            "$pressureInPSI ${context.getString(R.string.psi)}"
        }
    }

    val pressureInPSI: BigDecimal
        get() = getPressureInPsi()

    private fun getPressureInPsi() =
        (pressureInBar.times(BAR_TO_PSI_CONVERSION)).setScale(
            1,
            RoundingMode.HALF_EVEN
        )

    fun isEmpty() = pressureInBar != BigDecimal.ZERO
}