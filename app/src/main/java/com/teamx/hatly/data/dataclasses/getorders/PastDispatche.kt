package com.teamx.hatly.data.dataclasses.getorders

data class PastDispatche(
    val _id: String,
    val dropOff: DropOffX,
    val parcel: Parcel,
    val pickup: Pickup,
    val requestFor: String,
    val requestId: String,
    val status: String
)