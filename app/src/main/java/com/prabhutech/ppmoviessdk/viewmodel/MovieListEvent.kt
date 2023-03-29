package com.prabhutech.ppmoviessdk.viewmodel

import com.prabhutech.ppmoviessdk.core.utils.UiText
import com.prabhutech.ppmoviessdk.model.model.Movie

sealed class MovieListEvent {
    object Loading : MovieListEvent()
    class Success(val movies: List<Movie>) : MovieListEvent()
    class Failure(val errorTitle: UiText, val message: UiText) : MovieListEvent()
    object Complete: MovieListEvent()
}