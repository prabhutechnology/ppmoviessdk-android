package com.prabhutech.ppmoviessdk.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.prabhutech.ppmoviessdk.model.model.responsebody.MovieDate
import com.prabhutech.ppmoviessdk.view.MovieDateSelectFragment

class MovieDayTimePagerAdapter(
    fm: FragmentManager?,
    theaterDates: List<MovieDate>,
    processId: String,
    movieId: String
) :
    FragmentStatePagerAdapter(fm!!) {
    private val theaterDates: List<MovieDate>
    private val processId: String
    private val movieId: String

    init {
        this.theaterDates = theaterDates
        this.processId = processId
        this.movieId = movieId
    }

    override fun getItem(i: Int): Fragment =
        MovieDateSelectFragment(theaterDates[i].theaters, processId, movieId)

    override fun getCount(): Int = theaterDates.size
}