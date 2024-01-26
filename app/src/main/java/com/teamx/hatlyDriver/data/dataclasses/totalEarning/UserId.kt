package com.teamx.hatlyDriver.data.dataclasses.totalEarning
import androidx.annotation.Keep

@Keep
data class UserId(
    val __v: Int,
    val _id: String,
    val contact: String,
    val email: String,
    val name: String,
    val profileImage: String,
    val role: String,
    val verified: Boolean,
    val wallet: Double
)