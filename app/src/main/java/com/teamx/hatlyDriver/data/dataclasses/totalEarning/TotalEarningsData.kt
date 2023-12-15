package com.teamx.hatlyDriver.data.dataclasses.totalEarning

data class TotalEarningsData(
    val _id: String,
    val totalEarning: Double,
    val totalOrders: Double,
    val totalOrdersEarning: Double,
    val totalParcels: Double,
    val totalParcelsEarning: Double,
    val totalSpendTime: TotalSpendTime,
    val userId: UserId
)