package com.teamx.hatly.data.dataclasses.getorders

data class Veriation(
    val _id: String,
    val isMultiple: Boolean,
    val isRequired: Boolean,
    val limit: Int,
    val options: List<Option>,
    val title: String
)