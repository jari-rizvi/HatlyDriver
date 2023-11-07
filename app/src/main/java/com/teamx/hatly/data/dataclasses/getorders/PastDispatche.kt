package com.teamx.hatly.data.dataclasses.getorders

data class PastDispatche(
    val _id: String,
    val charges: Double,
    val dropOff: DropOff,
    val estimatedDeliveryTime: Any,
    val orders: Orders,
    val pickup: Pickup,
    val requestFor: String,
    val requestId: String,
    val riderId: RiderId,
    val shop: ShopX,
    val status: String
)