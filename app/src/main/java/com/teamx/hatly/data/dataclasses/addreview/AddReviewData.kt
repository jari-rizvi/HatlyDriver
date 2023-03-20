package com.teamx.hatly.dataclasses.addreview

import androidx.annotation.Keep


@Keep
data class AddReviewData(
    var comment: String,
    var photos: List<String>,
    var pivotId: String,
    var product: String,
    var rating: Int,
    var shop: String
)