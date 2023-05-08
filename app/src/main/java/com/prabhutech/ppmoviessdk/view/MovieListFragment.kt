package com.prabhutech.ppmoviessdk.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
    private var showErrorAlert: Dialog? = null

    @SuppressLint("notifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)

        binding.recyclerViewMoviesList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.swipeRefresh.setOnRefreshListener { moviesViewModel.getMovieShows() }

        _alertBoxBinding = AlertBoxBinding.inflate(inflater)

        showErrorAlert = Dialog(requireContext()).apply {
            setContentView(alertBoxBinding.root)
            setCancelable(true)
            setCanceledOnTouchOutside(true)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        var movieListAdapter = MovieListAdapter(requireContext(), "", emptyList())
        binding.recyclerViewMoviesList.adapter = movieListAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                moviesViewModel.movieShowsResponse.collectLatest { movieShows ->
                    when (movieShows) {
                        is MovieListEvent.Loading -> {
                            binding.recyclerViewMoviesList.visibility = View.VISIBLE
                            binding.containerNoMovie.noMovieLayout.visibility = View.GONE
                            binding.swipeRefresh.isRefreshing = true
                            movieListAdapter.setLoading(true)
                            movieListAdapter.notifyDataSetChanged()
                        }

                        is MovieListEvent.Success -> {
                            val processId = movieShows.processId
                            val movies = movieShows.movies

                            movieListAdapter = MovieListAdapter(requireContext(), processId, movies)
                            movieListAdapter.notifyDataSetChanged()

                            binding.recyclerViewMoviesList.adapter = movieListAdapter
                            binding.containerNoMovie.noMovieLayout.apply {
                                visibility =
                                    if (movieShows.movies.isEmpty()) View.VISIBLE else View.GONE
                            }
                        }

                        is MovieListEvent.Failure -> {
                            alertBoxBinding.apply {
                                textViewTitle.text = movieShows.errorTitle.asString(requireContext())
                                textViewMessage.text = movieShows.message.asString(requireContext())
                                btnCancel.setOnClickListener { showErrorAlert!!.dismiss() }
                                btnOk.text = getString(R.string.retry_title)
                                btnOk.setOnClickListener {
                                    moviesViewModel.getMovieShows()
                                    showErrorAlert!!.dismiss()
                                }
                            }
                            showErrorAlert!!.show()
                            binding.recyclerViewMoviesList.visibility = View.GONE
                            binding.containerNoMovie.noMovieLayout.visibility = View.VISIBLE
                        }

                        is MovieListEvent.Completed -> {
                            binding.swipeRefresh.isRefreshing = false
                            movieListAdapter.setLoading(false)
                            movieListAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        moviesViewModel.getMovieShows()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _alertBoxBinding = null
    }
}