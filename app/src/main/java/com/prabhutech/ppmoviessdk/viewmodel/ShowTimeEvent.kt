package com.prabhutech.ppmoviessdk.viewmodel

import com.prabhutech.ppmoviessdk.core.utils.UiText
import com.prabhutech.ppmoviessdk.model.model.responsebody.MovieShowTimeData

sealed class ShowTimeEvent {
    object Loading : ShowTimeEvent()
    class Success(val showTime: MovieShowTimeData) : ShowTimeEvent()
    class Failure(val errorTitle: UiText, val message: UiText) : ShowTimeEvent()
    object Completed : ShowTimeEvent()
}
