package com.prabhutech.ppmoviessdk.model.model.responsebody

import com.prabhutech.ppmoviessdk.model.model.Movie

data class MovieShowsData(
    val code: String,
    val message: String,
    val processId: String,
    val movies: List<Movie>
)