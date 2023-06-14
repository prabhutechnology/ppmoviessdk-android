package com.prabhutech.ppmoviessdk.model.model.getMoviesShows

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    @SerializedName("movieId") val id: String?,
    @SerializedName("movieName") val name: String?,
    @SerializedName("releaseDate") val releaseDate: String?,
    @SerializedName("genre") val genre: String?,
    @SerializedName("duration") val duration: String?,
    @SerializedName("poster") val poster: String?,
    @SerializedName("banner") val banner: String?,
    @SerializedName("synopsis") val synopsis: String?,
    @SerializedName("distributor") val distributor: String?,
    @SerializedName("director") val director: String?,
    @SerializedName("trailerVideo") val trailerVideo: String?,
    @SerializedName("casts") val casts: String?,
    @SerializedName("rating") val rating: String?,
    @SerializedName("price") val price: Int? = 0,
    @SerializedName("ticketType") val ticketType: String?,
    @SerializedName("status") val status: String?
) : Parcelable