package com.teamx.hatly.ui.fragments.chat.socket.model.incomingOrderSocketData

data class IncomingOrderSocketData(
    val _id: String,
    val charges: Int,
    val createdAt: String,
    val dropOff: DropOff,
    val estimatedDeliveryTime: Int,
    val order: String,
    val pickup: Pickup,
    val requestFor: String,
    val requestId: String,
    val riderId: Any,
    val shop: String,
    val status: String,
    val total: Double,
    val updatedAt: String
)