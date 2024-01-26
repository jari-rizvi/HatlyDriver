package com.teamx.hatlyDriver.data.dataclasses.getActiveorder
import androidx.annotation.Keep

@Keep
data class TradeLicence(
    val tradeLicenseExpireAt: String,
    val tradeLicenseIssueAt: String,
    val tradeLicenseUrl: String
)