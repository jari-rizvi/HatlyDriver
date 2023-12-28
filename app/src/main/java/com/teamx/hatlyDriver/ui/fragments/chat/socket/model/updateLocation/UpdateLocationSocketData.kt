package com.teamx.hatlyDriver.ui.fragments.chat.socket.model.updateLocation
import androidx.annotation.Keep

@Keep
data class UpdateLocationSocketData(
    val lat: Double,
    val lng: Double
)