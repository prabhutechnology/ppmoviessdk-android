package com.prabhutech.ppmoviessdk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prabhutech.ppmoviessdk.core.constant.AppConstant.MULTIPLE_MOVIE_TYPE
import com.prabhutech.ppmoviessdk.core.utils.DispatcherProvider
import com.prabhutech.ppmoviessdk.core.utils.Resource
import com.prabhutech.ppmoviessdk.model.model.requestbody.ShowTimeRequest
import com.prabhutech.ppmoviessdk.usecase.MovieShowTimeUseCase
import com.prabhutech.ppmoviessdk.usecase.MovieShowsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TOKEN =
    "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI5ODAxOTc5MDU4IiwianRpIjoiOTdjMjZhOTctODI5ZS00YzJjLThjMDAtYzc5ZGIzYzJmMWY4IiwiaWF0IjoxNjgyNDE5OTcyLCJJZCI6IjE3dkpUaGh4M2d3VDcvNjd1UnRraUxRSUVuSExVbEE4IiwiUm9sZSI6IkN1c3RvbWVyIiwibmJmIjoxNjgyMzMzNTcxLCJleHAiOjE2ODUwMTE5NzEsImlzcyI6IndlYkFwaSIsImF1ZCI6Imh0dHA6Ly9sb2NhbGhvc3Q6NTAwMC8ifQ.hMjR_KtFxjKXI6qJs9n6suV-YmqILU-0lPJ3bHAmd0I"

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val movieShowsUseCase: MovieShowsUseCase,
    private val movieShowTimeUseCase: MovieShowTimeUseCase
) : ViewModel() {
    private var processId: String = ""

    private val _movieShowsResponse = MutableSharedFlow<MovieListEvent>()
    val movieShowsResponse = _movieShowsResponse.asSharedFlow()

    private val _movieShowTimeResponse = MutableSharedFlow<ShowTimeEvent>()
    val movieShowTimeResponse = _movieShowTimeResponse.asSharedFlow()

    fun getMovieShows() {
        viewModelScope.launch(dispatcherProvider.io) {
            _movieShowsResponse.emit(MovieListEvent.Loading)
            when (val movieShowsResponse = movieShowsUseCase(TOKEN)) {
                is Resource.Success -> {
                    val movieList =
                        movieShowsResponse.data?.movies?.filter { it.ticketType?.lowercase() == MULTIPLE_MOVIE_TYPE }
                    processId = movieShowsResponse.data?.processId ?: ""
                    _movieShowsResponse.emit(
                        MovieListEvent.Success(
                            processId,
                            movieList ?: emptyList()
                        )
                    )
                }

                is Resource.Error ->
                    _movieShowsResponse.emit(
                        MovieListEvent.Failure(
                            errorTitle = movieShowsResponse.errorTitle!!,
                            message = movieShowsResponse.message!!
                        )
                    )
            }
            _movieShowsResponse.emit(MovieListEvent.Completed)
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
            _movieShowTimeResponse.emit(ShowTimeEvent.Completed)
        }
    }
}