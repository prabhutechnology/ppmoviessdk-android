package com.prabhutech.ppmoviessdk.model.model.responsebody

data class MovieShowTimeData(
    val banner: String,
    val casts: String,
    val code: String,
    val director: String,
    val distributor: String,
    val duration: String,
    val genre: String,
    val message: String,
    val movieDates: List<MovieDate>,
    val movieId: String,
    val movieName: String,
    val poster: String,
    val processId: String,
    val rating: String,
    val releaseDate: String,
    val synopsis: String,
    val trailerVideo: String
)