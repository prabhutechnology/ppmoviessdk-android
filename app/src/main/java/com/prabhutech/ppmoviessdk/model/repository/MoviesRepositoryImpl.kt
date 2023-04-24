package com.prabhutech.ppmoviessdk.model.repository

import com.prabhutech.ppmoviessdk.model.model.requestbody.MovieRequest
import com.prabhutech.ppmoviessdk.model.model.responsebody.MovieShowTimeResponse
import com.prabhutech.ppmoviessdk.model.model.getMoviesShows.MovieShowsResponse
import com.prabhutech.ppmoviessdk.model.remote.MoviesApi
import com.prabhutech.ppmoviessdk.core.utils.DispatcherProvider
import com.prabhutech.ppmoviessdk.core.utils.Resource
import com.prabhutech.ppmoviessdk.core.utils.SafeApiCall
import retrofit2.Response
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val api: MoviesApi,
    private val dispatcherProvider: DispatcherProvider,
) : MoviesRepository, SafeApiCall() {
    override suspend fun getMovieShows(token: String): Resource<Response<MovieShowsResponse>> =
        safeApiCall(dispatcherProvider) { api.getMovieShows(token) }

    override suspend fun getMovieShowTime(
        token: String,
        requestBody: MovieRequest
    ): Resource<Response<MovieShowTimeResponse>> = safeApiCall(dispatcherProvider) {
        api.getMovieShowTime(token, requestBody)
    }
}