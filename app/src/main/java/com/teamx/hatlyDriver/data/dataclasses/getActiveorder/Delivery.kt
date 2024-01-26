package com.teamx.hatlyDriver.data.dataclasses.getActiveorder
import androidx.annotation.Keep

@Keep
data class Delivery(
    val unit: String,
    val value: Int
)