package com.alpenraum.shimstack.data.bikeTemplates

import android.content.Context

interface BikeTemplateRepository {

    suspend fun prepopulateData(context: Context)

    suspend fun createBikeTemplates(list: List<BikeTemplate>)

    suspend fun getBikeTemplatesFilteredByName(name: String): List<BikeTemplate>
}
