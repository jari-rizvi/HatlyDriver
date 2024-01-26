package com.teamx.hatlyDriver.data.dataclasses.transactionHistory
import androidx.annotation.Keep

@Keep
data class DropOff(
    val _id: String,
    val additionalDirection: String,
    val address: String,
    val apartmentNumber: String,
    val building: String,
    val coordinates: Coordinates,
    val createdAt: String,
    val isDefault: Boolean,
    val isDeleted: Boolean,
    val label: String,
    val lat: Double,
    val lng: Double,
    val type: String,
    val userId: String
)