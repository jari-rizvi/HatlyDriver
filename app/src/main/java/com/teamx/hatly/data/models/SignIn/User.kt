package com.teamx.hatly.data.models.SignIn

data class User(
    val __v: Int,
    val _id: String,
    val address: List<Any>,
    val email: String,
    val is_active: Boolean,
    val name: String,
    val password: String,
    val profile: Profile,
    val roles: List<String>,
    val shops: List<Any>
)