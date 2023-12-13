package com.teamx.hatlyDriver.data.dataclasses.pastorder

data class Pickup(
    val city: String,
//    val coordinates: Coordinates,
    val country: String,
    val formattedAddress: String,
    val lat: Double,
    val lng: Double,
    val state: String,
    val type: String
)