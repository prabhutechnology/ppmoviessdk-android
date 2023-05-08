package com.prabhutech.ppmoviessdk.view

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.prabhutech.ppmoviessdk.databinding.FragmentMovieDetailBinding
import com.prabhutech.ppmoviessdk.databinding.InfoBoxBinding
import com.prabhutech.ppmoviessdk.viewmodel.MovieViewModel
import com.prabhutech.ppmoviessdk.viewmodel.ShowTimeEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MovieDetailFragment : Fragment() {
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private var _infoBoxBinding: InfoBoxBinding? = null
    private val infoBoxBinding get() = _infoBoxBinding!!

    private val moviesViewModel: MovieViewModel by activityViewModels()
    private val args: MovieDetailFragmentArgs by navArgs()
    private var showInfo: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        _infoBoxBinding = InfoBoxBinding.inflate(inflater)

        showInfo = Dialog(requireContext()).apply {
            setContentView(infoBoxBinding.root)
            setCancelable(true)
            setCanceledOnTouchOutside(true)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        val progressDialog = ProgressDialog(requireContext()).apply {
            setTitle("Loading")
            setMessage("Getting Movie showtime")
            setCancelable(false)
        }

        binding.btnInfo.setOnClickListener {
            val movie = args.movieDetail
            infoBoxBinding.apply {
                Glide.with(requireContext()).load(movie.poster)
                    .into(imageViewPoster)
                textViewInfoTitle.text = movie.name
                textViewInfoGenre.text = movie.genre
                textViewInfoTime.text = movie.duration
                textViewInfoDirector.text = movie.director
                textViewInfoSummary.text = movie.synopsis
            }

            showInfo!!.show()
        }

        binding.textViewName.text = args.movieDetail.name
        binding.textViewTime.text = args.movieDetail.duration
        binding.textViewGenre.text = args.movieDetail.genre
        Glide.with(requireContext()).load(args.movieDetail.banner)
            .into(binding.imageViewBanner)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                moviesViewModel.movieShowTimeResponse.collectLatest { showTime ->
                    when (showTime) {
                        is ShowTimeEvent.Loading -> {
                            //Show dialog
                            progressDialog.show()
                        }

                        is ShowTimeEvent.Success -> {
                            //Change view
//                            val movieShowTimeData = showTime.showTime
//                            binding.textViewName.text = movieShowTimeData.movieName
//                            binding.textViewTime.text = movieShowTimeData.duration
//                            binding.textViewGenre.text = movieShowTimeData.genre
//                            Glide.with(requireContext()).load(movieShowTimeData.banner)
//                                .into(binding.imageViewBanner)
                        }

                        is ShowTimeEvent.Failure -> {
                            //Show error
                        }

                        is ShowTimeEvent.Completed -> {
                            progressDialog.dismiss()
                        }
                    }
                }
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        moviesViewModel.getMovieShowTime(movieId = args.movieDetail.id ?: "0")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _infoBoxBinding = null
    }
}