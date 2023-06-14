package com.prabhutech.ppmoviessdk.view

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.prabhutech.ppmoviessdk.databinding.FragmentMovieDetailBinding
import com.prabhutech.ppmoviessdk.databinding.InfoBoxBinding
import com.prabhutech.ppmoviessdk.view.adapter.MovieDayTimePagerAdapter
import com.prabhutech.ppmoviessdk.viewmodel.MovieViewModel
import com.prabhutech.ppmoviessdk.viewmodel.ShowTimeEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
                            binding.tabLayoutDayTime.removeAllTabs()
                            for (date in showTime.showTime.movieDates) {
                                var date1 = Date()
                                val df = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                                try {
                                    date1 = df.parse(date.showDate)!!
                                } catch (e: ParseException) {
                                    e.printStackTrace()
                                }
                                val s =
                                    SimpleDateFormat("MMM dd\nEEE", Locale.ENGLISH).format(date1)

                                // increase font size of days
                                val ss = SpannableString(s.uppercase(Locale.getDefault()))
                                ss.setSpan(RelativeSizeSpan(1.2f), 7, 10, 0)
                                binding.tabLayoutDayTime.addTab(
                                    binding.tabLayoutDayTime.newTab().setText(ss)
                                )
                            }
                            // View pager
                            val moviesDaysListAdapter = MovieDayTimePagerAdapter(
                                childFragmentManager,
                                showTime.showTime.movieDates,
                                showTime.showTime.processId,
                                showTime.showTime.movieId
                            )
                            binding.viewPagerMovieList.adapter = moviesDaysListAdapter
                            binding.viewPagerMovieList.addOnPageChangeListener(
                                TabLayout.TabLayoutOnPageChangeListener(binding.tabLayoutDayTime)
                            )
                            binding.tabLayoutDayTime.addOnTabSelectedListener(
                                TabLayout.ViewPagerOnTabSelectedListener(binding.viewPagerMovieList)
                            )
                        }

                        is ShowTimeEvent.Failure -> {
                            //Show error
                            Toast.makeText(
                                requireContext(),
                                showTime.message.asString(requireContext()),
                                Toast.LENGTH_SHORT
                            )
                                .show()
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