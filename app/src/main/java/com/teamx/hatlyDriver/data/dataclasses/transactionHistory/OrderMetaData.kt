package com.teamx.hatlyDriver.data.dataclasses.transactionHistory

data class OrderMetaData(
    val _id: String,
    val clientSecret: String,
    val createdAt: String,
    val customer: String,
    val deliveryCharges: Double,
    val dropOff: DropOff,
    val isPayed: Boolean,
    val merchant: String,
    val orderId: String,
    val orderType: String,
    val payBy: String,
    val products: List<Product>,
    val shop: String,
    val specialNote: String,
    val status: String,
    val subTotal: Int,
    val tax: Double,
    val total: Double,
    val useWallet: Boolean,
    val usedWalletAmount: Double
)