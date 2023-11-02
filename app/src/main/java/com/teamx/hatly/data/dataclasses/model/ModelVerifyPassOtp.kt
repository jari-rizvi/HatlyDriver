package com.teamx.hatly.data.dataclasses.model

import androidx.annotation.Keep

@Keep
data class ModelVerifyPassOtp(
    val uniqueId: String,
    val verified: Boolean
)