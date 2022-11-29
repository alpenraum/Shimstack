package com.alpenraum.shimstack.data.bike

data class Suspension(
    val pressure: Double,
    val lscFromClosed: Int,
    val hscFromClosed: Int? = null,
    val lsrFromClosed: Int,
    val hsrFromClosed: Int? = null,
    val tokens: Int
)
