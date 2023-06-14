package com.prabhutech.ppmoviessdk.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prabhutech.ppmoviessdk.core.utils.NavigationRedirection
import com.prabhutech.ppmoviessdk.databinding.ItemMovieDateBinding
import com.prabhutech.ppmoviessdk.databinding.ItemMovieTimeBinding
import com.prabhutech.ppmoviessdk.model.model.responsebody.Theater
import com.prabhutech.ppmoviessdk.view.MovieDetailFragmentDirections

class MovieDateAdapter(
    private val context: Context,
    private val theaters: List<Theater>,
    private val processId: String,
    private val movieId: String
) : RecyclerView.Adapter<MovieDateAdapter.ViewHolder>() {
    inner class ViewHolder(val itemMovieTimeBinding: ItemMovieDateBinding) :
        RecyclerView.ViewHolder(itemMovieTimeBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemMovieDateBinding.inflate(LayoutInflater.from(context), parent, false)
    )

    override fun getItemCount(): Int = theaters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val theater = theaters[position]
        holder.itemMovieTimeBinding.apply {
            movieHallName.text =
                String.format(
                    "%s (%s)",
                    theater.theaterName,
                    theater.theaterAddress
                )
        }
        holder.itemMovieTimeBinding.flexBoxTime.removeAllViews()
        for (show in theater.movieShow) {
            val itemMovieTimeBinding = ItemMovieTimeBinding.inflate(LayoutInflater.from(context))
            itemMovieTimeBinding.time.text = show.showTime
            holder.itemMovieTimeBinding.flexBoxTime.addView(itemMovieTimeBinding.root)
            itemMovieTimeBinding.root.setOnClickListener {
                val movieDateArguments =
                    MovieDetailFragmentDirections.actionMovieDetailFragmentToBookSeatFragment(
                        show,
                        processId,
                        movieId
                    )
                NavigationRedirection.navigateToFragment(it, movieDateArguments)
            }
        }
    }
}