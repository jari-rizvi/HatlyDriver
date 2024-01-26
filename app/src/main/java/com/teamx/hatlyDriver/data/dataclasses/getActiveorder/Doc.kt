package com.teamx.hatlyDriver.data.dataclasses.getActiveorder
import androidx.annotation.Keep

@Keep
data class Doc(
    val _id: String,
    val averageRating: Int,
    val customer: Customer,
    val dropOff: DropOff,
    val orders: Orders,
    val pickup: Pickup,
    val rating: List<Rating>,
    val requestFor: String,
    val requestId: String,
    val riderId: String,
    val shop: ShopX,
    val status: String
)