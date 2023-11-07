package com.teamx.hatly.data.dataclasses.pastParcels

data class Parcel(
    val _id: String,
    val createdAt: String,
    val details: Details,
    val fare: Double,
    val receiverLocation: ReceiverLocation,
    val senderLocation: SenderLocation,
    val status: String,
    val trackingNumber: String,
    val updatedAt: String
)