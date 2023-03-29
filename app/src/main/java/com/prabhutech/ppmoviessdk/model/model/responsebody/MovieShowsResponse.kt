package com.prabhutech.ppmoviessdk.model.model.responsebody

import com.google.gson.annotations.SerializedName

data class MovieShowsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: MovieShowsData
)