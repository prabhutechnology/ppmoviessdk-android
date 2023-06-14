package com.prabhutech.ppmoviessdk.viewmodel

import com.prabhutech.ppmoviessdk.core.utils.UiText
import com.prabhutech.ppmoviessdk.model.model.getMoviesShows.Data

sealed class SeatLayoutEvent {
    object Loading : SeatLayoutEvent()
    class Success(val seatLayout: Data) : SeatLayoutEvent()
    class Failure(val errorTitle: UiText, val message: UiText) : SeatLayoutEvent()
}