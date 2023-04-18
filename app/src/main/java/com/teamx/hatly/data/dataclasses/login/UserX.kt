package com.teamx.hatly.data.dataclasses.login

import android.provider.ContactsContract

import androidx.annotation.Keep
import com.teamx.hatly.data.dataclasses.login.Addres


@Keep
data class UserX(
    val __v: Int,
    val _id: String,
    val address: List<Addres>,
    val createdAt: String,
    val email: String,
    val is_active: Boolean,
    val contact_verified: Boolean,
    var contact: String,
    val email_verified: Boolean,
    val name: String,
    val password: String,
    val profile: ContactsContract.Profile,
    val roles: List<String>,
    val shops: List<Any>,
    val favouritShop: List<String>?,
    val updatedAt: String
)