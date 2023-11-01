package com.teamx.hatly.ui.fragments.Dashboard.notification.modelNotification
import androidx.annotation.Keep

@Keep
data class Doc(
    val _id: String,
    val description: String,
    val isRead: Boolean,
    val title: String,
    val userId: String,
    var createdAt: String
)