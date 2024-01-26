package com.teamx.hatlyDriver.data.dataclasses.transactionHistory
import androidx.annotation.Keep

@Keep
data class SenderLocation(
    val location: Location,
    val locationId: String,
    val phoneNumber: String
)