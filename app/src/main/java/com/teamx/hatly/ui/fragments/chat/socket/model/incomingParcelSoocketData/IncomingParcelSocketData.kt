package com.teamx.hatly.ui.fragments.chat.socket.model.incomingParcelSoocketData

data class IncomingParcelSocketData(
    val _id: String,
    val createdAt: String,
    val dropOff: DropOff,
    val parcel: Parcel,
    val pickup: Pickup
)