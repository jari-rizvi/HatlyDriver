package com.teamx.hatly.ui.fragments.chat.socket.model.incomingOrderSocketData

data class IncomingOrderSocketData(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val customer: String,
    val deliveryCharges: Double,
    val dropOff: DropOff,
    val isPayed: Boolean,
    val merchant: String,
    val orderId: String,
    val orderType: String,
    val products: List<Product>,
    val shop: Shop,
    val specialNote: String,
    val status: String,
    val subTotal: Int,
    val tax: Int,
    val total: Double,
    val updatedAt: String,
    val useWallet: Boolean,
    val usedWalletAmount: String
)