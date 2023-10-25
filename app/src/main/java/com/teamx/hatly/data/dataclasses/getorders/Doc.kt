package com.teamx.hatly.data.dataclasses.getorders

data class Doc(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val customer: String,
    val deliveryCharges: Double,
    val isPayed: Boolean,
    val merchant: String,
    val orderId: String,
    val orderType: String,
    val products: List<Product>,
    val shippingAddress: ShippingAddress,
    val shop: Shop,
    val specialNote: String,
    val status: String,
    val subTotal: Int,
    val tax: Double,
    val total: Double,
    val useWallet: Boolean,
    val usedWalletAmount: Double
)