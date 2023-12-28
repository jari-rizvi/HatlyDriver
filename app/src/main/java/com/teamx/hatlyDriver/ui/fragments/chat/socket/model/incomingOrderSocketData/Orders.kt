package com.teamx.hatlyDriver.ui.fragments.chat.socket.model.incomingOrderSocketData
import androidx.annotation.Keep

@Keep
data class Orders(
    val __v: Int,
    val _id: String,
    val orderId: String,
    val products: List<Product>,

)