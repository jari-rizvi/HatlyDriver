package com.teamx.hatly.data.models.socket_models
import androidx.annotation.Keep

@Keep
data class ExceptionData(
    val message: Any, val type: String
)