package com.teamx.hatlyDriver.ui.fragments.chat.socket.model.incomingOrderSocketData

data class Shop(
    val _id: String,
    val address: Address,
    val delivery: Delivery,
    val products: List<Any>,
)