package com.teamx.hatly.data.models

data class MusicResponseModel(
    val resultCount: Int,
    val results: List<MusicModel>?,
)