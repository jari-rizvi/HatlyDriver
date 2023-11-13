package com.teamx.hatly.data.dataclasses.pastorder

data class Orders(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val customer: Customer,
    val deliveryCharges: Double,
    val isPayed: Boolean,
    val merchant: String,
    val orderId: String,
    val orderType: String,
    val products: List<Product>,
    val specialNote: String,
    val status: String,
    val subTotal: Int,
    val tax: Int,
    val total: Double,
    val updatedAt: String,
    val useWallet: Boolean,
    val usedWalletAmount: Int
)