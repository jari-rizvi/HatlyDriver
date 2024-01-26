package com.teamx.hatlyDriver.data.dataclasses.getActiveorder
import androidx.annotation.Keep

@Keep
data class Setting(
    val closesAt: String,
    val contact: String,
    val location: LocationX,
    val opensAt: String
)