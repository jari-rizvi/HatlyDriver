package com.teamx.hatly.ui.fragments.topUp.model.savedCard

import androidx.annotation.Keep

@Keep
data class ModelSavedCard(
    val _id: String,
    val amount: Int,
    val clientSecret: String,
    val customer: String,
    val isPayed: Boolean
)