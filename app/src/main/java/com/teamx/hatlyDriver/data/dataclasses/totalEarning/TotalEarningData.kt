package com.teamx.hatlyDriver.data.dataclasses.totalEarning

data class TotalEarningData(
    val riderDetail: RiderDetail,
    val spentTime: SpentTime,
    val totalEarning: Int,
    val totalOrder: Int,
    val totalParcel: Int
)