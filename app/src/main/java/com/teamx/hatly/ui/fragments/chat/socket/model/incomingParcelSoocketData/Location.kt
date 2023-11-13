package com.teamx.hatly.ui.fragments.chat.socket.model.incomingParcelSoocketData

data class Location(
    val address: String,
    val coordinates: Coordinates,
    val type: String
)