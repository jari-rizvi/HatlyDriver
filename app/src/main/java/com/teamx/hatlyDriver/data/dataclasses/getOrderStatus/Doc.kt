package com.teamx.hatlyDriver.data.dataclasses.getOrderStatus

import androidx.annotation.Keep


@Keep
data class Doc(
    val _id: String,
    val dropOff: DropOff,
    val parcel: Parcel,
    val pickup: Pickup,
    val rejectionReason: String,
    val requestFor: String,
    val requestId: String,
    val riderId: String,
    val status: String
)