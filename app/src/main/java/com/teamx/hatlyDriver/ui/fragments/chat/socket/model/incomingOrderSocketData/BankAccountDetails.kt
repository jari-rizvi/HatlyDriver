package com.teamx.hatlyDriver.ui.fragments.chat.socket.model.incomingOrderSocketData
import androidx.annotation.Keep

@Keep
data class BankAccountDetails(
    val accountHolderEmail: String,
    val accountHolderName: String,
    val accountNumber: String,
    val bankName: String
)