package com.teamx.hatly.ui.fragments.chat.socket.model.incomingOrderSocketData

data class Doc(
    val _id: String,
    val charges: Int,
    val createdAt: String,
    val dropOff: DropOff,
    val estimatedDeliveryTime: Any,
    val orders: Orders,
    val pickup: Pickup,
    val requestFor: String,
    val requestId: String? = "",
    val riderId: String? = "",
    val shop: Shop,
    val status: String,
    val total: Double,
    val updatedAt: String
)

