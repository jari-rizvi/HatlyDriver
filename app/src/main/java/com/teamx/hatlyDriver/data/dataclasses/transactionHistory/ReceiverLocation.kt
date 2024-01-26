package com.teamx.hatlyDriver.data.dataclasses.transactionHistory
import androidx.annotation.Keep

@Keep
data class ReceiverLocation(
    val location: Location,
    val locationId: String,
    val phoneNumber: String
)