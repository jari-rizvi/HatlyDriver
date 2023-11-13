package com.teamx.hatly.data.dataclasses.pastorder

data class Doc(
    val _id: String,
    val charges: Double,
    val createdAt: String,
    val dropOff: DropOff,
    val estimatedDeliveryTime: Double,
    val orders: Orders,
    val pickup: Pickup,
    val requestFor: String,
    val requestId: String,
    val riderId: String,
    val shop: Shop,
    val status: String,
    val total: Double,
    val updatedAt: String
)