package com.teamx.hatlyDriver.data.dataclasses.sucess
import androidx.annotation.Keep

@Keep
data class SuccessData(
    val message: String,
    val success: Boolean
)