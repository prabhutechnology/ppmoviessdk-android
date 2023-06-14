package com.prabhutech.ppmoviessdk.usecase

import com.prabhutech.ppmoviessdk.core.constant.AppConstant.ERROR_MESSAGE
import com.prabhutech.ppmoviessdk.core.constant.AppConstant.TRY_AGAIN_MESSAGE
import com.prabhutech.ppmoviessdk.core.utils.Resource
import com.prabhutech.ppmoviessdk.core.utils.UiText
import com.prabhutech.ppmoviessdk.model.model.requestbody.ShowTimeRequest
import com.prabhutech.ppmoviessdk.model.model.responsebody.MovieShowTimeData
import com.prabhutech.ppmoviessdk.model.repository.MoviesRepository
import javax.inject.Inject

class MovieShowTimeUseCase @Inject constructor(private val moviesRepository: MoviesRepository) {
    suspend operator fun invoke(
        token: String,
        movieShowTimeRequest: ShowTimeRequest
    ): Resource<MovieShowTimeData> {
        return when (val movieShowTimeResponse =
            moviesRepository.getMovieShowTime(token, movieShowTimeRequest)) {
            is Resource.Success -> {
                val response = movieShowTimeResponse.data
                if (response == null || !response.isSuccessful) {
                    return Resource.Error(
                        errorTitle = ERROR_MESSAGE,
                        message = UiText.DynamicString(response!!.message())
                    )
                }
                response.body()?.let {
                    if (it.success) {
                        Resource.Success(it.data)
                    } else {
                        Resource.Error(
                            errorTitle = ERROR_MESSAGE,
                            message = UiText.DynamicString(it.message)
                        )
                    }
                } ?: Resource.Error(
                    errorTitle = movieShowTimeResponse.errorTitle ?: ERROR_MESSAGE,
                    message = movieShowTimeResponse.message ?: TRY_AGAIN_MESSAGE
                )
            }

            is Resource.Error -> {
                Resource.Error(
                    errorTitle = movieShowTimeResponse.errorTitle ?: ERROR_MESSAGE,
                    message = movieShowTimeResponse.message ?: TRY_AGAIN_MESSAGE
                )
            }
        }
    }
}