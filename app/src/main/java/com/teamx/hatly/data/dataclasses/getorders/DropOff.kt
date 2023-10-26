package com.teamx.hatly.data.dataclasses.getorders

data class DropOff(
    val _id: String,
    val addressType: String,
    val apartmentNumber: Int,
    val area: String,
    val building: String,
    val floor: Int,
    val userId: String
)