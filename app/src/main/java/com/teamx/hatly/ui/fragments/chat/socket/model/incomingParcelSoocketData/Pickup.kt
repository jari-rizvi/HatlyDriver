package com.teamx.hatly.ui.fragments.chat.socket.model.incomingParcelSoocketData

data class Pickup(
    val address: String,
    val coordinates: Coordinates,
    val type: String
)