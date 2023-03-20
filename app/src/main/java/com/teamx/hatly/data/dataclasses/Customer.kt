package com.teamx.hatly.dataclasses

import androidx.annotation.Keep


@Keep
data class Customer(
    val __v: Int,
    val _id: String,
    val address: List<String>,
    val contact: String,
    val contact_verified: Boolean,
    val createdAt: String,
    val email: String,
    val email_verified: Boolean,
    val is_active: Boolean,
    val name: String,
    val password: String,
    val profile: ProfileX,
    val roles: List<String>,
    val shops: List<Any>,
    val updatedAt: String
)