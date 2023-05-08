package com.prabhutech.ppmoviessdk.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.prabhutech.ppmoviessdk.R
import com.prabhutech.ppmoviessdk.core.utils.NavigationRedirection
import com.prabhutech.ppmoviessdk.databinding.ItemMovieBinding
import com.prabhutech.ppmoviessdk.databinding.ItemMovieLoadingBinding
import com.prabhutech.ppmoviessdk.model.model.getMoviesShows.Movie
import com.prabhutech.ppmoviessdk.view.MovieListFragmentDirections

class MovieListAdapter(
    private val context: Context,
    private val processId: String,
    private val movies: List<Movie>? = emptyList()
) :
    RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {
    private var loading = true

    companion object {
        private const val VIEW_TYPE_LOADING = 0
        private const val VIEW_TYPE_DATA = 1
    }

    class ViewHolder : RecyclerView.ViewHolder {
        var binding: ItemMovieBinding? = null

        constructor(binding: ItemMovieBinding) : super(binding.root) {
            this.binding = binding
        }

        constructor(shimmerBinding: ItemMovieLoadingBinding) : super(shimmerBinding.root)
    }

    fun setLoading(loading: Boolean) {
        this@MovieListAdapter.loading = loading
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        if (viewType == VIEW_TYPE_LOADING) {
            ViewHolder(
                ItemMovieLoadingBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
            )
        } else {
            ViewHolder(
                ItemMovieBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
            )
        }

    override fun getItemViewType(position: Int): Int =
        if (loading) VIEW_TYPE_LOADING
        else VIEW_TYPE_DATA

    override fun getItemCount(): Int = if (loading) 6 else movies?.size ?: 0

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_TYPE_DATA) {
            val movieName = movies?.get(position)?.name ?: "N/A"
            val moviePrice = movies?.get(position)?.price ?: "0.00"
            val moviePoster = movies?.get(position)?.poster ?: ""
            holder.binding!!.apply {
                textViewMovieName.text = movieName
                if (moviePrice == "0.00") {
                    textViewMoviePrice.visibility = View.VISIBLE
                    val priceFormat = "NPR. $moviePrice/-"
                    textViewMoviePrice.text = priceFormat
                }
                val placeHolderImage =
                    ContextCompat.getDrawable(context, R.drawable.img_placeholder)
                val requestOptions = RequestOptions()
                requestOptions.placeholder(placeHolderImage)
                requestOptions.error(placeHolderImage)

                Glide.with(context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(moviePoster)
                    .into(imageMovie)
            }

            holder.binding!!.containerMovie.setOnClickListener {
                val movieListArguments =
                    MovieListFragmentDirections.actionMovieListToDetail(
                        processId = processId,
                        movieDetail = movies?.get(position)!!
                    )
                NavigationRedirection.navigateToFragment(it, movieListArguments)
            }
        }
    }
}