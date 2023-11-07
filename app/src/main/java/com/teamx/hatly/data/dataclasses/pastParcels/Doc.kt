package com.teamx.hatly.data.dataclasses.pastParcels

data class Doc(
    val _id: String,
    val createdAt: String,
    val estimatedDeliveryTime: Int,
    val parcel: Parcel,
    val requestFor: String,
    val requestId: String,
    val riderId: String,
    val status: String,
    val updatedAt: String
)