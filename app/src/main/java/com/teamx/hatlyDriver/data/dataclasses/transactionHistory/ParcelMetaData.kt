package com.teamx.hatlyDriver.data.dataclasses.transactionHistory

data class ParcelMetaData(
    val _id: String,
    val createdAt: String,
    val details: Details,
    val driver: String,
    val fare: Double,
    val receiverLocation: ReceiverLocation,
    val senderId: String,
    val senderLocation: SenderLocation,
    val status: String,
    val trackingNumber: String
)