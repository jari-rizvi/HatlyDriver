package com.teamx.hatly.data.dataclasses.login

import androidx.annotation.Keep
import com.teamx.hatly.data.dataclasses.login.UserXX


@Keep
data class User(
    val permissions: List<String>,
    val token: String,
    val user: UserXX
)