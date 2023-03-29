package com.prabhutech.ppmoviessdk.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.prabhutech.ppmoviessdk.databinding.FragmentMovieListBinding
import com.prabhutech.ppmoviessdk.view.adapter.MovieListAdapter
import com.prabhutech.ppmoviessdk.viewmodel.MovieListEvent
import com.prabhutech.ppmoviessdk.viewmodel.MovieViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MovieListFragment : Fragment() {
    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!
    private val moviesViewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        binding.recyclerViewMoviesList.layoutManager = GridLayoutManager(context, 2)
        binding.swipeRefresh.setOnRefreshListener { moviesViewModel.getMovieShows() }
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var movieListAdapter = MovieListAdapter(requireContext(), emptyList())
        binding.recyclerViewMoviesList.adapter = movieListAdapter
        moviesViewModel.getMovieShows()

        viewLifecycleOwner.lifecycleScope.launch {
            moviesViewModel.movieShowsResponse.collectLatest { movieShows ->
                when (movieShows) {
                    is MovieListEvent.Loading -> {
                        binding.containerNoMovie.noMovieLayout.visibility = View.GONE
                        binding.swipeRefresh.isRefreshing = true
                        movieListAdapter.setLoading(true)
                        movieListAdapter.notifyDataSetChanged()
                    }
                    is MovieListEvent.Success -> {
                        movieListAdapter = MovieListAdapter(requireContext(), movieShows.movies)
                        movieListAdapter.notifyDataSetChanged()
                        binding.recyclerViewMoviesList.adapter = movieListAdapter
                        binding.containerNoMovie.noMovieLayout.apply {
                            visibility =
                                if (movieShows.movies.isEmpty()) View.VISIBLE else View.GONE
                        }
                    }
                    is MovieListEvent.Failure -> {
                        binding.recyclerViewMoviesList.visibility = View.GONE
                        binding.containerNoMovie.noMovieLayout.visibility = View.VISIBLE
                        Log.e(
                            "MovieListEvent.Failure",
                            "onViewCreated: ${movieShows.message.asString(requireContext())}"
                        )
                    }
                    is MovieListEvent.Complete -> {
                        binding.swipeRefresh.isRefreshing = false
                        movieListAdapter.setLoading(false)
                        movieListAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}