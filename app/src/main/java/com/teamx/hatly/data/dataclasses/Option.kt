package com.teamx.hatly.dataclasses

import androidx.annotation.Keep


@Keep
data class Option(
    val _id: String,
    val createdAt: String,
    val name: String,
    val updatedAt: String,
    val value: String
)