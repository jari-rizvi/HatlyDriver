package com.teamx.hatly.data.dataclasses.forgotPass
import androidx.annotation.Keep

@Keep
data class forgotPassPhoneData(
    val email: String,
    val message: String,
    val response: Response,
    val success: Boolean
)