package com.alpenraum.shimstack.data.bikeTemplates

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import javax.inject.Inject

class LocalBikeTemplateRepository
@Inject
constructor(
    private val bikeTemplateDAO: BikeTemplateDAO,
    private val moshi: Moshi
) : BikeTemplateRepository {
    @OptIn(ExperimentalStdlibApi::class)
    override suspend fun prepopulateData(context: Context) {
        val adapter: JsonAdapter<List<BikeTemplate>> = moshi.adapter()
        val file = "bike_templates.json"
        val json = context.assets.open(file).bufferedReader().use { it.readText() }

        adapter.fromJson(json)?.let { createBikeTemplates(it) }
    }

    override suspend fun createBikeTemplates(list: List<BikeTemplate>) {
        bikeTemplateDAO.insertBike(list)
    }

    override suspend fun getBikeTemplatesFilteredByName(name: String): List<BikeTemplate> {
        return bikeTemplateDAO.getBikeTemplatesFilteredByName(name)
    }
}