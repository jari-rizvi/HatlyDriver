package com.teamx.hatly.data.dataclasses.getorders

data class Pickup(
    val address: String,
    val coordinates: Coordinates,
    val type: String
)