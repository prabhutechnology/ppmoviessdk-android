package com.prabhutech.ppmoviessdk.viewmodel

import com.prabhutech.ppmoviessdk.core.utils.UiText
import com.prabhutech.ppmoviessdk.model.model.getMoviesShows.Movie

sealed class MovieListEvent {
    object Loading : MovieListEvent()
    class Success(val processId: String, val movies: List<Movie>) : MovieListEvent()
    class Failure(val errorTitle: UiText, val message: UiText) : MovieListEvent()
    object Completed : MovieListEvent()
}
