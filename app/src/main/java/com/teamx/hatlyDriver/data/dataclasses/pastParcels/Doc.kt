package com.teamx.hatlyDriver.data.dataclasses.pastParcels

import androidx.annotation.Keep


@Keep
data class Doc(
    val _id: String,
    val estimatedDeliveryTime: Any,
    val parcel: Parcel,
    val requestFor: String,
    val requestId: String,
    val riderId: String,
    val status: String
)