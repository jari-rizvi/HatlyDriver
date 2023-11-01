package com.teamx.hatly.data.dataclasses.getorders

data class DropOff(
    val address: String,
    val coordinates: Coordinates,
    val type: String
)