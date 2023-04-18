package com.teamx.hatly.data.dataclasses.forgotPass
import androidx.annotation.Keep

@Keep
data class Response(
    val id: String,
    val message: String,
    val phone_number: String,
    val provider: String,
    val success: Boolean,
    val twilio: Twilio
)