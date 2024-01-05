package com.alpenraum.shimstack.data.db.typeconverter

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalTypeConverter {
    @TypeConverter
    fun fromString(value: String?): BigDecimal? {
        return value?.let { BigDecimal(it) }
    }

    @TypeConverter
    fun toString(value: BigDecimal?): String? {
        return value?.toString()
    }
}