package com.teamx.hatlyDriver.data.dataclasses.meModel.me
import androidx.annotation.Keep

@Keep
data class BankDetail(
    val accountHolderName: String,
    val bankName: String,
    val accountNumber: String
)