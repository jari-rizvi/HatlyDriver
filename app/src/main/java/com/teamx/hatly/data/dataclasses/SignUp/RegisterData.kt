package com.teamx.hatly.data.models.SignUp

import androidx.annotation.Keep

@Keep
data class RegisterData(
    val id: String,
    val message: String,
    val phone_number: String,
    val provider: String,
    val success: Boolean,
    val twilio: Twilio
)