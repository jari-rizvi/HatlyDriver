package com.teamx.hatlyDriver.data.dataclasses.getActiveorder
import androidx.annotation.Keep

@Keep
data class BankAccountDetails(
    val accountHolderEmail: String,
    val accountHolderName: String,
    val accountNumber: String,
    val bankName: String
)