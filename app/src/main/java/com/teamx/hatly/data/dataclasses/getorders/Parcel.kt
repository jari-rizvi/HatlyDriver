package com.teamx.hatly.data.dataclasses.getorders

data class Parcel(
    val _id: String,
    val details: Details,
    val fare: Int,
    val receiverLocation: ReceiverLocation,
    val senderId: String,
    val senderLocation: SenderLocation,
    val status: String,
    val trackingNumber: String
)