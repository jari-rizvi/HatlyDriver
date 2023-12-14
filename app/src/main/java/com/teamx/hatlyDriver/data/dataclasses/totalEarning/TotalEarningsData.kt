package com.teamx.hatlyDriver.data.dataclasses.totalEarning

data class TotalEarningsData(
    val _id: String,
    val totalEarning: Int,
    val totalOrders: Int,
    val totalOrdersEarning: Int,
    val totalParcels: Int,
    val totalParcelsEarning: Int,
    val totalSpendTime: TotalSpendTime,
    val userId: UserId
)