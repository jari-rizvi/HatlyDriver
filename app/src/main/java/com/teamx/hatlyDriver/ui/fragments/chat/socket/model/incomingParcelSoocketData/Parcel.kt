package com.teamx.hatlyDriver.ui.fragments.chat.socket.model.incomingParcelSoocketData

data class Parcel(
    val fare: Double,
    val receiverLocation: ReceiverLocation,
    val senderLocation: SenderLocation,
)