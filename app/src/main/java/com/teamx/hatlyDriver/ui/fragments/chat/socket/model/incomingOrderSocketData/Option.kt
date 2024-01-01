package com.teamx.hatlyDriver.ui.fragments.chat.socket.model.incomingOrderSocketData
import androidx.annotation.Keep

@Keep
data class Option(
    val _id: String,
    val name: String,
    val price: Int,
    val salePrice: Int
)