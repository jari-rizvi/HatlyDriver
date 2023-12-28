package com.teamx.hatlyDriver.ui.fragments.chat.socket.model.incomingOrderSocketData
import androidx.annotation.Keep

@Keep
data class Product(
    val _id: String,
    val image: String,
    val productId: String,
    val productName: String,
    val quantity: Int,
)