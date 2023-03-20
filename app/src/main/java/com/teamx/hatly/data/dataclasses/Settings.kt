package com.teamx.hatly.dataclasses

import androidx.annotation.Keep


@Keep
data class Settings(
    val _id: String,
    val socials: List<Any>
)