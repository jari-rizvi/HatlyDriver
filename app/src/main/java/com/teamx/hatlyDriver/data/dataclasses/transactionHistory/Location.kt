package com.teamx.hatlyDriver.data.dataclasses.transactionHistory

data class Location(
    val _id: String,
    val additionalDirection: String,
    val address: String,
    val building: String,
    val coordinates: Coordinates,
    val createdAt: String,
    val id: String,
    val isDefault: Boolean,
    val isDeleted: Boolean,
    val label: String,
    val lat: Double,
    val lng: Double,
    val type: String,
    val userId: String
)