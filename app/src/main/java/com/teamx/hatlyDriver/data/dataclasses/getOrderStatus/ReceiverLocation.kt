package com.teamx.hatlyDriver.data.dataclasses.getOrderStatus
import androidx.annotation.Keep


@Keep
data class ReceiverLocation(
    val locationId: LocationId,
    val phoneNumber: String
)