package com.teamx.hatlyDriver.ui.fragments.chat.socket.model.incomingOrderSocketData
import androidx.annotation.Keep

@Keep
data class Shop(
    val _id: String,
    val address: Address,
    val delivery: Delivery,
    val products: List<Any>,
)