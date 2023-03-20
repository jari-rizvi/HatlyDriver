package com.teamx.hatly.dataclasses

import androidx.annotation.Keep

 
@Keep
data class AddressByID(
    val __v: Int,
    val _id: String,
    var address: Address,
    val createdAt: String,
    val customer: Customer,
    val default: Boolean,
    var title: String,
    var type: String,
    val updatedAt: String
)