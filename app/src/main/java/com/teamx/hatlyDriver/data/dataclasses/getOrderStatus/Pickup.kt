package com.teamx.hatlyDriver.data.dataclasses.getOrderStatus
import androidx.annotation.Keep


@Keep
data class Pickup(
    val _id: String,
    val address: String,
    val coordinates: Coordinates,
    val isDefault: Boolean,
    val label: String,
    val lat: Double,
    val lng: Double,
    val type: String,
    val userId: String
)