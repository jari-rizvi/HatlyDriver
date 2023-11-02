package com.teamx.hatly.data.dataclasses.getorders

data class GetAllOrdersData(
    val incomingRequests: List<Any>,
    val pastDispatches: List<PastDispatche>
)