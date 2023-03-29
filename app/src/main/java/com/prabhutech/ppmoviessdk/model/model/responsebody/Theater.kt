package com.prabhutech.ppmoviessdk.model.model.responsebody

data class Theater(
    val movieShow: List<MovieShow>,
    val theaterAddress: String,
    val theaterLogo: String,
    val theaterName: String
)