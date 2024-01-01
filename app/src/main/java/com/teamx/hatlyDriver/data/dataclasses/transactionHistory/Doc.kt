package com.teamx.hatlyDriver.data.dataclasses.transactionHistory

data class Doc(
    val _id: String,
    val amount: Double,
    val change: String,
    val createdAt: String,
    val `for`: String,
    val id: String,
    val orderMetaData: OrderMetaData,
    val parcelMetaData: ParcelMetaData,
    val payBy: String,
    val status: String,
    val totalAmount: Double,
    val transectionGatewayId: String,
    val useWallet: Boolean,
    val userId: String,
    val walletAmount: Double,
    val walletMetaData: WalletMetaData,
    val walletType: String
)