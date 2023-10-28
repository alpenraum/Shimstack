package com.alpenraum.shimstack.data.bikeTemplates

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.alpenraum.shimstack.data.db.AppDatabase

@Dao
interface BikeTemplateDAO {

    @Insert
    fun insertBike(bikes: List<BikeTemplate>)

    @Query(
        "SELECT * FROM ${AppDatabase.table_bike_template} WHERE name LIKE '%' || :searchTerm || '%'"
    )
    fun getBikeTemplatesFilteredByName(searchTerm: String?): List<BikeTemplate>
}
