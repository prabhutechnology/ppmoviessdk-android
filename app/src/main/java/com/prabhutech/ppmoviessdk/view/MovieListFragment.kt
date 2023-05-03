package com.prabhutech.ppmoviessdk.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.prabhutech.ppmoviessdk.R
import com.prabhutech.ppmoviessdk.databinding.AlertBoxBinding
import com.prabhutech.ppmoviessdk.databinding.FragmentMovieListBinding
import com.prabhutech.ppmoviessdk.view.adapter.MovieListAdapter
import com.prabhutech.ppmoviessdk.viewmodel.MovieListEvent
import com.prabhutech.ppmoviessdk.viewmodel.MovieViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MovieListFragment : Fragment() {
    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private var _alertBoxBinding: AlertBoxBinding? = null
    private val alertBoxBinding get() = _alertBoxBinding!!

    private val moviesViewModel: MovieViewModel by activityViewModels()
    private val showErrorAlert by lazy {
        Dialog(requireContext()).apply {
            setContentView(alertBoxBinding.root)
            setCancelable(true)
            setCanceledOnTouchOutside(true)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moviesViewModel.getMovieShows()
    }

    @SuppressLint("notifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)

        binding.recyclerViewMoviesList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.swipeRefresh.setOnRefreshListener { moviesViewModel.getMovieShows() }

        _alertBoxBinding = AlertBoxBinding.inflate(inflater)

        var movieListAdapter = MovieListAdapter(requireContext(), "", emptyList())
        binding.recyclerViewMoviesList.adapter = movieListAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            moviesViewModel.movieShowsResponseTest.collectLatest { movieShows ->
                when (movieShows) {
                    is MovieListEvent.Loading -> {
                        binding.recyclerViewMoviesList.visibility = View.VISIBLE
                        binding.containerNoMovie.noMovieLayout.visibility = View.GONE
                        binding.swipeRefresh.isRefreshing = true
                        movieListAdapter.setLoading(true)
                        movieListAdapter.notifyDataSetChanged()
                    }

                    is MovieListEvent.Success -> {
                        movieListAdapter = MovieListAdapter(
                            context = requireContext(),
                            processId = movieShows.processId,
                            movies = movieShows.movies
                        )
                        binding.recyclerViewMoviesList.adapter = movieListAdapter
                        binding.containerNoMovie.noMovieLayout.apply {
                            visibility =
                                if (movieShows.movies.isEmpty()) View.VISIBLE else View.GONE
                        }

                        binding.swipeRefresh.isRefreshing = false
                        movieListAdapter.setLoading(false)
                        movieListAdapter.notifyDataSetChanged()
                    }

                    is MovieListEvent.Failure -> {
                        alertBoxBinding.apply {
                            textViewTitle.text = movieShows.errorTitle.asString(requireContext())
                            textViewMessage.text = movieShows.message.asString(requireContext())
                            btnCancel.setOnClickListener { showErrorAlert.dismiss() }
                            btnOk.text = getString(R.string.retry_title)
                            btnOk.setOnClickListener {
                                moviesViewModel.getMovieShows()
                                showErrorAlert.dismiss()
                            }
                        }
                        showErrorAlert.show()
                        binding.recyclerViewMoviesList.visibility = View.GONE
                        binding.containerNoMovie.noMovieLayout.visibility = View.VISIBLE

                        binding.swipeRefresh.isRefreshing = false
                        movieListAdapter.setLoading(false)
                        movieListAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _alertBoxBinding = null
    }
}