package com.teamx.hatly.dataclasses

import androidx.annotation.Keep

 
@Keep
data class Address(
    var _id: String,
    var city: String,
    var country: String,
    var state: String,
    var street_address: String,
    var zip: String
)