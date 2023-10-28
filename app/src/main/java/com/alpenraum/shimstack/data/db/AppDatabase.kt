package com.alpenraum.shimstack.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alpenraum.shimstack.data.bike.Bike
import com.alpenraum.shimstack.data.bike.BikeDAO
import com.alpenraum.shimstack.data.bikeTemplates.BikeTemplate
import com.alpenraum.shimstack.data.bikeTemplates.BikeTemplateDAO
import com.alpenraum.shimstack.data.db.typeconverter.BigDecimalTypeConverter

@Database(entities = [Bike::class, BikeTemplate::class], version = 2, exportSchema = false)
@TypeConverters(BigDecimalTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bikeDao(): BikeDAO
    abstract fun bikeTemplateDao(): BikeTemplateDAO

    companion object {
        const val table_bike = "user_bikes"
        const val table_bike_template = "template_bikes"
    }
}
