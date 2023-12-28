package com.teamx.hatlyDriver.data.dataclasses.withdrawalHistory
import androidx.annotation.Keep

@Keep
data class Doc(
    val _id: String,
    val amount: Int,
    val status: String,
    val userId: String,
    val createdAt: String
)