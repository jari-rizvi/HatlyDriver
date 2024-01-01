package com.teamx.hatlyDriver.ui.fragments.chat.socket.model.incomingParcelSoocketData
import androidx.annotation.Keep

@Keep
data class Location(
    val address: String,
    val coordinates: Coordinates,
    val type: String
)