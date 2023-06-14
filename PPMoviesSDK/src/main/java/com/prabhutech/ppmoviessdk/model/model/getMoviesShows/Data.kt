package com.prabhutech.ppmoviessdk.model.model.getMoviesShows

data class Data(
    val code: String,
    val duration: String,
    val genre: String,
    val holdTime: String,
    val maxSeatSelection: String,
    val message: String,
    val movieId: String,
    val movieName: String,
    val movieSeatRows: List<MovieSeatRow>,
    val processId: String,
    val screenName: String,
    val showDate: String,
    val showId: String,
    val showTime: String,
    val theaterAddress: String,
    val theaterLogo: String,
    val theaterName: String
)