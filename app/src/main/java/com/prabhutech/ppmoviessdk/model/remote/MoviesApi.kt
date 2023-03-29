package com.prabhutech.ppmoviessdk.model.remote

import com.prabhutech.ppmoviessdk.model.model.requestbody.MovieRequest
import com.prabhutech.ppmoviessdk.model.model.responsebody.MovieShowTimeResponse
import com.prabhutech.ppmoviessdk.model.model.responsebody.MovieShowsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MoviesApi {
    companion object {
        private const val GET_MOVIE_SHOWS = "BillPayment/GetMovieShows"
        private const val GET_MOVIE_SHOW_TIME = "BillPayment/MovieShowTime"
        private const val GET_MOVIE_SEAT_LAYOUT = "BillPayment/MovieSeatLayout"
        private const val GET_HOLD_MOVIE_SEAT = "BillPayment/HoldMovieSeat"
        private const val GET_RELEASE_MOVIE_SEAT = "BillPayment/ReleaseMovieSeat"
        private const val GET_ISSUE_MOVIE_TICKET = "BillPayment/IssueMovieTicket"
        private const val GET_RELEASE_ALL_MOVIE_SEATS = "ReleaseAllMovieSeats"
    }

    @GET(GET_MOVIE_SHOWS)
    suspend fun getMovieShows(@Header("Authorization") token: String): Response<MovieShowsResponse>

    @POST(GET_MOVIE_SHOW_TIME)
    suspend fun getMovieShowTime(
        @Header("Authorization") token: String,
        @Body requestBody: MovieRequest
    ): Response<MovieShowTimeResponse>
}