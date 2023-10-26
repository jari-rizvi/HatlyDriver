package com.teamx.hatly.data.dataclasses.getorders

data class GetAllOrdersData(
    val incomingRequests: List<IncomingRequest>,
    val pastDispatches: List<PastDispatche>
)