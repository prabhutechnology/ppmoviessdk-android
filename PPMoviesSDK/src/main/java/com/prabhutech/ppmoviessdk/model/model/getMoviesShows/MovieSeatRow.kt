package com.prabhutech.ppmoviessdk.model.model.getMoviesShows

data class MovieSeatRow(
    val category: String,
    val movieSeates: List<MovieSeate>,
    val rowName: String
)