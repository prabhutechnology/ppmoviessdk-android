package com.prabhutech.ppmoviessdk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prabhutech.ppmoviessdk.core.constant.AppConstant.MULTIPLE_MOVIE_TYPE
import com.prabhutech.ppmoviessdk.core.utils.DispatcherProvider
import com.prabhutech.ppmoviessdk.core.utils.Resource
import com.prabhutech.ppmoviessdk.model.model.requestbody.SeatRequest
import com.prabhutech.ppmoviessdk.model.model.requestbody.ShowTimeRequest
import com.prabhutech.ppmoviessdk.usecase.MovieSeatLayoutUseCase
import com.prabhutech.ppmoviessdk.usecase.MovieShowTimeUseCase
import com.prabhutech.ppmoviessdk.usecase.MovieShowsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TOKEN =
    "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI5ODAxOTc5MDU4IiwianRpIjoiYTA2ZjJiODAtMzY3ZC00MzM5LWI5Y2MtMzg5NjA2OTgwYTA2IiwiaWF0IjoxNjg2NzI0Nzg5LCJJZCI6IjE3dkpUaGh4M2d3VDcvNjd1UnRraUxRSUVuSExVbEE4IiwiUm9sZSI6IkN1c3RvbWVyIiwibmJmIjoxNjg2NjM4Mzg5LCJleHAiOjE2ODY4OTc1ODksImlzcyI6IndlYkFwaSIsImF1ZCI6Imh0dHA6Ly9sb2NhbGhvc3Q6NTAwMC8ifQ.QXzl8TCs8BZPbbQ3eAGJe9KANArkVZxGeX2xj3DaRgo"

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val movieShowsUseCase: MovieShowsUseCase,
    private val movieShowTimeUseCase: MovieShowTimeUseCase,
    private val movieSeatLayoutUseCase: MovieSeatLayoutUseCase
) : ViewModel() {
    private var processId: String = ""

    private val _movieShowStateFlow = MutableStateFlow<MovieListEvent>(MovieListEvent.Loading)
    val movieShowsStateFlow = _movieShowStateFlow.asStateFlow()

    private val _movieShowTimeResponse = MutableStateFlow<ShowTimeEvent>(ShowTimeEvent.Loading)
    val movieShowTimeResponse = _movieShowTimeResponse.asStateFlow()

    private val _seatLayoutResponse = MutableStateFlow<SeatLayoutEvent>(SeatLayoutEvent.Loading)
    val seatLayoutResponse = _seatLayoutResponse.asStateFlow()

    fun getSeatLayout(seatRequest: SeatRequest) {
        viewModelScope.launch(dispatcherProvider.io) {
            when (val seatLayoutResponse = movieSeatLayoutUseCase(TOKEN, seatRequest)) {
                is Resource.Success -> {
                    _seatLayoutResponse.emit(SeatLayoutEvent.Success(seatLayoutResponse.data!!))
                }

                is Resource.Error -> {
                    _seatLayoutResponse.emit(
                        SeatLayoutEvent.Failure(
                            errorTitle = seatLayoutResponse.errorTitle!!,
                            message = seatLayoutResponse.message!!
                        )
                    )
                }
            }
        }
    }

    fun getMovieShows() {
        viewModelScope.launch(dispatcherProvider.io) {
            when (val movieShowsResponse = movieShowsUseCase(TOKEN)) {
                is Resource.Success -> {
                    val movieList =
                        movieShowsResponse.data?.movies?.filter { it.ticketType?.lowercase() == MULTIPLE_MOVIE_TYPE }
                    processId = movieShowsResponse.data?.processId ?: ""
                    _movieShowStateFlow.emit(
                        MovieListEvent.Success(processId, movieList ?: emptyList())
                    )
                }

                is Resource.Error ->
                    _movieShowStateFlow.emit(
                        MovieListEvent.Failure(
                            errorTitle = movieShowsResponse.errorTitle!!,
                            message = movieShowsResponse.message!!
                        )
                    )
            }
        }
    }

    fun getMovieShowTime(movieId: String) {
        viewModelScope.launch(dispatcherProvider.io) {
            _movieShowTimeResponse.emit(ShowTimeEvent.Loading)
            when (val movieShowTimeResponse = movieShowTimeUseCase.invoke(
                TOKEN,
                ShowTimeRequest(movieId = movieId, processId = processId)
            )) {
                is Resource.Success -> {
                    _movieShowTimeResponse.emit(ShowTimeEvent.Success(movieShowTimeResponse.data!!))
                }

                is Resource.Error -> _movieShowTimeResponse.emit(
                    ShowTimeEvent.Failure(
                        errorTitle = movieShowTimeResponse.errorTitle!!,
                        message = movieShowTimeResponse.message!!
                    )
                )
            }
        }
    }
}