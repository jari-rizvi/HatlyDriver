package com.teamx.hatly.data.dataclasses.getorders

data class IncomingRequest(
    val _id: String,
    val dropOff: DropOff,
    val estimatedDeliveryTime: Int,
    val parcel: Parcel,
    val pickup: Pickup,
    val requestFor: String,
    val requestId: String,
    val status: String
)