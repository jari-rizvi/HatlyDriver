package com.teamx.hatly.ui.fragments.chat.socket.model.incomingOrderSocketData

data class Product(
    val _id: String,
    val image: String,
    val prize: Int,
    val productId: String,
    val productName: String,
    val quantity: Int,
    val shopId: String,
    val veriations: Any
)