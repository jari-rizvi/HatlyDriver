package com.teamx.hatly.data.dataclasses.getorders

data class ShippingAddress(
    val additionalDirection: String,
    val addressType: String,
    val apartmentNumber: Int,
    val area: String,
    val building: String,
    val floor: Int,
    val streat: String
)