package com.teamx.hatly.data.models.otpVerifyForgot

import androidx.annotation.Keep


@Keep
data class OtpVerifyForgotData(
    val message: String,
    val success: Boolean,
    val token: String
)