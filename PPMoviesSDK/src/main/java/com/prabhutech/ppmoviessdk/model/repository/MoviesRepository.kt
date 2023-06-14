package com.prabhutech.ppmoviessdk.model.repository

import com.prabhutech.ppmoviessdk.model.model.responsebody.MovieShowTimeResponse
import com.prabhutech.ppmoviessdk.model.model.getMoviesShows.MovieShowsResponse
import com.prabhutech.ppmoviessdk.core.utils.Resource
import com.prabhutech.ppmoviessdk.model.model.getMoviesShows.SeatResponse
import com.prabhutech.ppmoviessdk.model.model.requestbody.SeatRequest
import com.prabhutech.ppmoviessdk.model.model.requestbody.ShowTimeRequest
import retrofit2.Response

interface MoviesRepository {
    suspend fun getMovieShows(token: String): Resource<Response<MovieShowsResponse>>

    suspend fun getMovieShowTime(
        token: String,
        requestBody: ShowTimeRequest
    ): Resource<Response<MovieShowTimeResponse>>

    suspend fun getMovieSeatLayout(
        token: String,
        requestBody: SeatRequest
    ): Resource<Response<SeatResponse>>
}