package com.teamx.hatlyDriver.data.dataclasses.transactionHistory

data class Product(
    val _id: String,
    val image: String,
    val prize: Int,
    val productId: String,
    val productName: String,
    val quantity: Int,
    val shopId: String,
    val specialInstruction: String,
    val veriations: Any
)