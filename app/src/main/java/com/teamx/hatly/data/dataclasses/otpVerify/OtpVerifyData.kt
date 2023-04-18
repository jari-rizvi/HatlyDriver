package com.teamx.hatly.data.models.otpVerify

import androidx.annotation.Keep


@Keep
data class OtpVerifyData(
    val permissions: List<String>,
    val token: String,
    val user: User
)