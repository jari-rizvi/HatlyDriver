package com.teamx.hatly.data.dataclasses.login


import android.provider.ContactsContract
import androidx.annotation.Keep

@Keep
data class UserXX(
    val __v: Int,
    val _id: String,
    val contact: String,
    val contact_verified: Boolean,
    val createdAt: String,
    val email: String,
    val email_verified: Boolean,
    val is_active: Boolean,
    val name: String,
    val password: String,
    val profile: ContactsContract.Profile,
    val roles: List<String>,
    val shops: List<Any>,
    val updatedAt: String
)