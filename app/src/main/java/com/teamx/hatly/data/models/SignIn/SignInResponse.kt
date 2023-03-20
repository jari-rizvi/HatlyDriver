package com.teamx.hatly.data.models.SignIn

data class SignInResponse(
    val permissions: List<String>,
    val token: String,
    val user: User
)