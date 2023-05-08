package com.prabhutech.ppmoviessdk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prabhutech.ppmoviessdk.core.constant.AppConstant.MULTIPLE_MOVIE_TYPE
import com.prabhutech.ppmoviessdk.core.utils.DispatcherProvider
import com.prabhutech.ppmoviessdk.core.utils.Resource
import com.prabhutech.ppmoviessdk.usecase.MovieShowsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val movieShowsUseCase: MovieShowsUseCase
) : ViewModel() {
    private val _movieShowsResponseFlow = MutableSharedFlow<MovieListEvent>()
    val movieShowsResponse = _movieShowsResponseFlow.asSharedFlow()

    fun getMovieShows() {
        viewModelScope.launch(dispatcherProvider.io) {
            _movieShowsResponseFlow.emit(MovieListEvent.Loading)
            when (val movieShowsResponse =
                movieShowsUseCase("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI5ODAxOTc5MDU4IiwianRpIjoiOTdjMjZhOTctODI5ZS00YzJjLThjMDAtYzc5ZGIzYzJmMWY4IiwiaWF0IjoxNjgyNDE5OTcyLCJJZCI6IjE3dkpUaGh4M2d3VDcvNjd1UnRraUxRSUVuSExVbEE4IiwiUm9sZSI6IkN1c3RvbWVyIiwibmJmIjoxNjgyMzMzNTcxLCJleHAiOjE2ODUwMTE5NzEsImlzcyI6IndlYkFwaSIsImF1ZCI6Imh0dHA6Ly9sb2NhbGhvc3Q6NTAwMC8ifQ.hMjR_KtFxjKXI6qJs9n6suV-YmqILU-0lPJ3bHAmd0I")) {
                is Resource.Success -> {
                    val movieList =
                        movieShowsResponse.data?.movies?.filter { it.ticketType?.lowercase() == MULTIPLE_MOVIE_TYPE }
                    _movieShowsResponseFlow.emit(
                        MovieListEvent.Success(
                            movieShowsResponse.data?.processId ?: "",
                            movieList ?: emptyList()
                        )
                    )
                }

                is Resource.Error ->
                    _movieShowsResponseFlow.emit(
                        MovieListEvent.Failure(
                            errorTitle = movieShowsResponse.errorTitle!!,
                            message = movieShowsResponse.message!!
                        )
                    )
            }
            _movieShowsResponseFlow.emit(MovieListEvent.Completed)
        }
    }
}