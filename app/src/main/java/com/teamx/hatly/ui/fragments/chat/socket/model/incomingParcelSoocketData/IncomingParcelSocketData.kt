package com.teamx.hatly.ui.fragments.chat.socket.model.incomingParcelSoocketData

data class IncomingParcelSocketData(
    val _id: String,
    val charges: Int,
    val createdAt: String,
    val estimatedDeliveryTime: Any,
    val parcel: Parcel,
    val requestFor: String,
    val requestId: String,
    val riderId: Any,
    val status: String,
    val updatedAt: String
)