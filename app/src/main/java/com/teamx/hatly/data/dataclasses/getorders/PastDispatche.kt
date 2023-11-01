package com.teamx.hatly.data.dataclasses.getorders

data class PastDispatche(
    val _id: String,
//    val dropOff: DropOffX,
    val estimatedDeliveryTime: Int,
    val parcel: ParcelX,
//    val pickup: PickupX,
    val requestFor: String,
    val requestId: String,
    val riderId: String,
    val status: String
)