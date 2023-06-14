package com.prabhutech.ppmoviessdk.model.model.getMoviesShows

import com.google.gson.annotations.SerializedName

data class MovieShowsData(
    @SerializedName("code") val code: String? = "",
    @SerializedName("message") val message: String? = "",
    @SerializedName("processId") val processId: String? = "",
    @SerializedName("movies") val movies: List<Movie>? = emptyList()
)