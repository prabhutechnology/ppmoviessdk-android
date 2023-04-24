package com.prabhutech.ppmoviessdk.model.repository

import com.prabhutech.ppmoviessdk.model.model.requestbody.MovieRequest
import com.prabhutech.ppmoviessdk.model.model.responsebody.MovieShowTimeResponse
import com.prabhutech.ppmoviessdk.model.model.getMoviesShows.MovieShowsResponse
import com.prabhutech.ppmoviessdk.core.utils.Resource
import retrofit2.Response

interface MoviesRepository {
    suspend fun getMovieShows(token: String): Resource<Response<MovieShowsResponse>>

    suspend fun getMovieShowTime(
        token: String,
        requestBody: MovieRequest
    ): Resource<Response<MovieShowTimeResponse>>
}