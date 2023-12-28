package com.teamx.hatlyDriver.data.dataclasses.pastorder
import androidx.annotation.Keep

@Keep
data class Delivery(
    val unit: String,
    val value: Int
)