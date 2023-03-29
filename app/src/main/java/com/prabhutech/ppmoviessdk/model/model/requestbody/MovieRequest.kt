package com.prabhutech.ppmoviessdk.model.model.requestbody

data class MovieRequest(
    val seatCategory: String? = "",
    val seatId: String? = "",
    val showId: String? = "",
    val movieId: String,
    val processId: String
)