package com.teamx.hatlyDriver.data.dataclasses.withdrawalDetails
import androidx.annotation.Keep

@Keep
data class WithDrawalDetailsData(
    val _id: String,
    val amount: Int,
    val screenShort: String,
    val status: String,
    val userId: String
)