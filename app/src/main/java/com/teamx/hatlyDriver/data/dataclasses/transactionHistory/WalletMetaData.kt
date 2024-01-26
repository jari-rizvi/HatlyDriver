package com.teamx.hatlyDriver.data.dataclasses.transactionHistory
import androidx.annotation.Keep

@Keep
data class WalletMetaData(
    val afterAmount: Double,
    val beforeAmount: Int
)