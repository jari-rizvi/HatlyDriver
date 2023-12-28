package com.teamx.hatlyDriver.ui.fragments.chat.socket.model.incomingOrderSocketData
import androidx.annotation.Keep

@Keep
data class Doc(
    val _id: String,
    val dropOff: DropOff,
    val orders: Orders,
    val pickup: Pickup,
    val shop: Shop,
    val total: Double,
)

