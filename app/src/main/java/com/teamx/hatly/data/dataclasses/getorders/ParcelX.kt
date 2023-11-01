package com.teamx.hatly.data.dataclasses.getorders

data class ParcelX(
    val _id: Id,
    val createdAt: String,
//    val details: DetailsX,
    val fare: Int,
    val receiverLocation: ReceiverLocationX,
    val senderId: SenderId,
    val senderLocation: SenderLocationX,
    val status: String,
    val trackingNumber: String,
    val updatedAt: String
)