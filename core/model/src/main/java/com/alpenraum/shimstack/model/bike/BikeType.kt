package com.alpenraum.shimstack.model.bike

import androidx.annotation.StringRes
import com.alpenraum.shimstack.model.R

enum class BikeType(
    val id: Int,
    @StringRes val labelRes: Int
) {
    GRAVEL(1, R.string.label_gravel_type),
    XC(
        2,
        R.string.label_xc_type
    ),
    TRAIL(
        3,
        R.string.label_trail_type
    ),
    ALL_MTN(4, R.string.label_all_mtn_type),
    ENDURO(5, R.string.label_enduro_type),
    DH(
        6,
        R.string.label_dh_type
    ),
    ROAD(7, R.string.label_road_type),
    UNKNOWN(
        0,
        R.string.label_unknown_type
    );

    companion object {
        fun fromId(id: Int) = entries.firstOrNull { it.id == id } ?: UNKNOWN
    }
}