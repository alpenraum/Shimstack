package com.alpenraum.shimstack.core.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alpenraum.shimstack.core.database.dao.BikeDAO
import com.alpenraum.shimstack.core.database.dao.BikeTemplateDAO
import com.alpenraum.shimstack.core.database.db.typeconverter.BigDecimalTypeConverter
import com.alpenraum.shimstack.core.database.models.BikeDTO
import com.alpenraum.shimstack.core.database.models.BikeTemplateDTO

@Database(
    entities = [BikeDTO::class, BikeTemplateDTO::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(BigDecimalTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bikeDao(): BikeDAO

    abstract fun bikeTemplateDao(): BikeTemplateDAO

    companion object {
        const val TABLE_BIKE = "user_bikes"
        const val TABLE_BIKE_TEMPLATE = "template_bikes"
    }
}