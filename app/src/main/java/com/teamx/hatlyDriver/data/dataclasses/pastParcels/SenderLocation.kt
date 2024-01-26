package com.teamx.hatlyDriver.data.dataclasses.pastParcels
import androidx.annotation.Keep


@Keep
data class SenderLocation(
    val location: Location,
    val phoneNumber: String
)