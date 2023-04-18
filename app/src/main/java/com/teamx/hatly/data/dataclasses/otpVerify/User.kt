package com.teamx.hatly.data.models.otpVerify

import androidx.annotation.Keep


@Keep
data class User(
    val __v: Int,
    val _id: String,
    val address: List<Any>,
    val contact: String,
    val contact_verified: Boolean,
    val createdAt: String,
    val email_verified: Boolean,
    val is_active: Boolean,
    val name: String,
    val password: String,
    val roles: List<String>,
    val shops: List<Any>,
    val updatedAt: String
)