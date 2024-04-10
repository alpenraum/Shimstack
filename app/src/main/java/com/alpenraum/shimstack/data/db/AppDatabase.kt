package com.alpenraum.shimstack.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alpenraum.shimstack.data.bike.BikeDAO
import com.alpenraum.shimstack.data.bikeTemplates.BikeTemplateDAO
import com.alpenraum.shimstack.data.db.typeconverter.BigDecimalTypeConverter
import com.alpenraum.shimstack.data.models.bike.BikeDTO
import com.alpenraum.shimstack.data.models.biketemplate.BikeTemplateDTO

@Database(entities = [BikeDTO::class, BikeTemplateDTO::class], version = 2, exportSchema = false)
@TypeConverters(BigDecimalTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bikeDao(): BikeDAO

    abstract fun bikeTemplateDao(): BikeTemplateDAO

    companion object {
        const val TABLE_BIKE = "user_bikes"
        const val TABLE_BIKE_TEMPLATE = "template_bikes"
    }
}