package com.prabhutech.ppmoviessdk.usecase

import com.prabhutech.ppmoviessdk.R
import com.prabhutech.ppmoviessdk.core.utils.Resource
import com.prabhutech.ppmoviessdk.core.utils.UiText
import com.prabhutech.ppmoviessdk.model.model.Movie
import com.prabhutech.ppmoviessdk.model.repository.MoviesRepository
import javax.inject.Inject

class MovieShowsUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {
    suspend operator fun invoke(token: String): Resource<List<Movie>> =
        when (val movieShowsResponse = moviesRepository.getMovieShows(token)) {
            is Resource.Success -> {
                val response = movieShowsResponse.data
                val result = response!!.body()
                if (response.isSuccessful && result != null) {
                    if (result.success) {
                        Resource.Success(result.data.movies)
                    } else Resource.Error(
                        errorTitle = UiText.StringResource(R.string.something_went_wrong),
                        message = UiText.DynamicString(result.message),
                    )
                } else Resource.Error(
                    errorTitle = UiText.StringResource(R.string.something_went_wrong),
                    message = UiText.DynamicString(response.message()),
                )
            }
            is Resource.Error -> {
                Resource.Error(
                    errorTitle = UiText.StringResource(R.string.something_went_wrong),
                    message = movieShowsResponse.message
                        ?: UiText.StringResource(resId = R.string.something_went_wrong)
                )
            }
        }
}