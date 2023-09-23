package com.alpenraum.shimstack.data.bike

import android.content.Context
import androidx.room.Ignore
import com.alpenraum.shimstack.R
import java.math.BigDecimal
import java.math.RoundingMode

data class Pressure(val pressureInBar: BigDecimal) {

    @Ignore
    constructor(pressureInBar: Double) : this(pressureInBar.toBigDecimal())

    companion object {
        val BAR_TO_PSI_CONVERSION = BigDecimal(14.503773773)
    }

    fun toFormattedString(context: Context): String {
        // TODO: add settings for pressure scale
        return if (true) {
            "$pressureInBar ${context.getString(R.string.bar)}"
        } else {
            "$pressureInPSI ${context.getString(R.string.psi)}"
        }
    }

    val pressureInPSI: BigDecimal
        get() = getPressureInPsi()

    private fun getPressureInPsi() = (pressureInBar.times(BAR_TO_PSI_CONVERSION)).setScale(
        1,
        RoundingMode.HALF_EVEN
    )
}