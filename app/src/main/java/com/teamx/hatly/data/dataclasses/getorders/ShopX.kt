package com.teamx.hatly.data.dataclasses.getorders

data class ShopX(
    val _id: Id,
    val address: Address,
    val balance: Balance,
    val bankAccountDetails: BankAccountDetails,
    val createdAt: String,
    val delivery: Delivery,
    val image: String,
    val isEnabled: Boolean,
    val isOpen: Boolean,
    val name: String,
    val owner: Owner,
    val products: List<Any>,
    val rank: Int,
    val ratting: String,
    val resturantCategory: List<String>,
    val setting: Setting,
    val shopCategory: List<Any>,
    val status: String,
    val totalRating: Double,
    val totalReviews: Int,
    val tradeLicence: TradeLicence,
    val type: String,
    val updatedAt: String
)