package com.teamx.hatlyDriver.data.dataclasses.withdrawalData
import androidx.annotation.Keep

@Keep
data class WithDrawalData(
    val _id: String,
    val amount: Int,
    val status: String,
    val userId: String
)