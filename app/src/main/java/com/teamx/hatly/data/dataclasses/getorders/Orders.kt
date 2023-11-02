package com.teamx.hatly.data.dataclasses.getorders

data class Orders(
    val __v: Int,
    val _id: Id,
    val createdAt: String,
    val customer: Customer,
    val deliveryCharges: Double,
    val dropOff: DropOffX,
    val isPayed: Boolean,
    val merchant: Merchant,
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