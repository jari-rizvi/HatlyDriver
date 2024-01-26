package com.teamx.hatlyDriver.data.dataclasses.getActiveorder
import androidx.annotation.Keep

@Keep
data class Pickup(
    val city: String,
    val coordinates: List<Double>,
    val country: String,
    val formattedAddress: String,
    val lat: Double,
    val lng: Double,
    val state: String,
    val type: String
)