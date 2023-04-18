package com.teamx.hatly.data.dataclasses.login

import androidx.annotation.Keep


@Keep
data class LoginData(
    val permissions: List<String>,
    val token: String,
    val user: UserX
)