package com.teamx.hatlyDriver.data.dataclasses.pastParcels
import androidx.annotation.Keep


@Keep
data class Location(
    val address: String,
    val coordinates: Coordinates,
    val type: String
)