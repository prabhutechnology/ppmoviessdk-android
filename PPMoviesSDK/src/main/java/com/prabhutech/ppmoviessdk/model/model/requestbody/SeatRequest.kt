package com.prabhutech.ppmoviessdk.model.model.requestbody

import com.google.gson.annotations.SerializedName

data class SeatRequest(
    @SerializedName("showId") val showId: String,
    @SerializedName("movieId") val movieId: String,
    @SerializedName("processId") val processId: String
)
