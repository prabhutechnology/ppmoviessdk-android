package com.prabhutech.ppmoviessdk.model.model.responsebody

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieShow(
    val screenName: String,
    val showId: String,
    val showTime: String
): Parcelable