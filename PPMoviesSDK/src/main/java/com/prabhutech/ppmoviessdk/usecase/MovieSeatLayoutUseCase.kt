package com.prabhutech.ppmoviessdk.usecase

import com.prabhutech.ppmoviessdk.core.constant.AppConstant
import com.prabhutech.ppmoviessdk.core.utils.Resource
import com.prabhutech.ppmoviessdk.core.utils.UiText
import com.prabhutech.ppmoviessdk.model.model.getMoviesShows.Data
import com.prabhutech.ppmoviessdk.model.model.requestbody.SeatRequest
import com.prabhutech.ppmoviessdk.model.repository.MoviesRepository
import javax.inject.Inject

class MovieSeatLayoutUseCase @Inject constructor(private val moviesRepository: MoviesRepository) {
    suspend operator fun invoke(token: String, seatRequest: SeatRequest): Resource<Data> {
        return when (val movieSeatLayoutResponse =
            moviesRepository.getMovieSeatLayout(token, seatRequest)) {
            is Resource.Success -> {
                val response = movieSeatLayoutResponse.data
                if (response == null || !response.isSuccessful) {
                    return Resource.Error(
                        errorTitle = AppConstant.ERROR_MESSAGE,
                        message = UiText.DynamicString(response!!.message())
                    )
                }
                response.body()?.let {
                    if (it.success) {
                        Resource.Success(it.data)
                    } else {
                        Resource.Error(
                            errorTitle = AppConstant.ERROR_MESSAGE,
                            message = UiText.DynamicString(it.message)
                        )
                    }
                } ?: Resource.Error(
                    errorTitle = movieSeatLayoutResponse.errorTitle ?: AppConstant.ERROR_MESSAGE,
                    message = movieSeatLayoutResponse.message ?: AppConstant.TRY_AGAIN_MESSAGE
                )
            }

            is Resource.Error -> {
                Resource.Error(
                    errorTitle = movieSeatLayoutResponse.errorTitle ?: AppConstant.ERROR_MESSAGE,
                    message = movieSeatLayoutResponse.message ?: AppConstant.TRY_AGAIN_MESSAGE
                )
            }
        }
    }
}