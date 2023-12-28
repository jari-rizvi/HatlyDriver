package com.teamx.hatlyDriver.ui.fragments.chat.socket.model.incomingOrderSocketData
import androidx.annotation.Keep

@Keep
data class Address(
    val city: String,
    val country: String,
    val state: String,
    val streetAddress: String,
    val zip: Int
)