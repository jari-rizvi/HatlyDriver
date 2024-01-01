package com.teamx.hatlyDriver.data.dataclasses.pastorder
import androidx.annotation.Keep

@Keep
data class BankAccountDetails(
    val accountHolderEmail: String,
    val accountHolderName: String,
    val accountNumber: String,
    val bankName: String
)