package com.teamx.hatly.dataclasses

import androidx.annotation.Keep

 
@Keep
data class SettingsX(
    val isHome: Boolean,
    val layoutType: String,
    val productCard: String
)