package com.alpenraum.shimstack.data.models.bike

import androidx.annotation.StringRes
import com.alpenraum.shimstack.R

enum class BikeType(
    @StringRes val labelRes: Int
) {
    ROAD(R.string.label_road_type),
    GRAVEL(R.string.label_gravel_type),
    XC(
        R.string.label_xc_type
    ),
    TRAIL(
        R.string.label_trail_type
    ),
    ALL_MTN(R.string.label_all_mtn_type),
    ENDURO(R.string.label_enduro_type),
    DH(
        R.string.label_dh_type
    ),
    UNKNOWN(
        R.string.label_unknown_type
    )
}