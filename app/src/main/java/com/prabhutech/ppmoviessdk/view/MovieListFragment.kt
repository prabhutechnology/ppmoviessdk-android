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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moviesViewModel.getMovieShows()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        _alertBoxBinding = AlertBoxBinding.inflate(inflater)

        showErrorAlert = Dialog(requireContext()).apply {
            setContentView(alertBoxBinding.root)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        binding.swipeRefresh.setOnRefreshListener { moviesViewModel.getMovieShows() }

        var movieListAdapter = MovieListAdapter(requireContext(), "", emptyList())
        binding.recyclerViewMoviesList.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = movieListAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                moviesViewModel.movieShowsStateFlow.collectLatest {
                    when (it) {
                        is MovieListEvent.Loading -> {
                            binding.recyclerViewMoviesList.visibility = View.VISIBLE
                            binding.containerNoMovie.noMovieLayout.visibility = View.GONE
                            refreshAdapter(movieListAdapter, true)
                        }

                        is MovieListEvent.Success -> {
                            val processId = it.processId
                            val movies = it.movies

                            movieListAdapter = MovieListAdapter(requireContext(), processId, movies)
                            binding.recyclerViewMoviesList.adapter = movieListAdapter
                            refreshAdapter(movieListAdapter, false)
                            binding.containerNoMovie.noMovieLayout.apply {
                                visibility = if (it.movies.isEmpty()) View.VISIBLE else View.GONE
                            }
                            binding.recyclerViewMoviesList.visibility = View.VISIBLE
                        }

                        is MovieListEvent.Failure -> {
                            alertBoxBinding.apply {
                                textViewTitle.text = it.errorTitle.asString(requireContext())
                                textViewMessage.text = it.message.asString(requireContext())
                                btnCancel.setOnClickListener {
                                    refreshAdapter(movieListAdapter, false)
                                    showErrorAlert!!.dismiss()
                                }
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
                    }
                }
            }
        }
        return binding.root
    }

    @SuppressLint("notifyDataSetChanged")
    fun refreshAdapter(movieListAdapter: MovieListAdapter, isRefresh: Boolean) {
        binding.swipeRefresh.isRefreshing = isRefresh
        movieListAdapter.setLoading(isRefresh)
//        movieListAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _alertBoxBinding = null
    }
}