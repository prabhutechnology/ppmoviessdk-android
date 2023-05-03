package com.prabhutech.ppmoviessdk.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.prabhutech.ppmoviessdk.R
import com.prabhutech.ppmoviessdk.databinding.FragmentMovieDetailBinding

private const val TAG = "MovieDetailFragment"
class MovieDetailFragment : Fragment() {
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG, "onViewCreated: ${args.movieDetail}" )
        Log.e(TAG, "onViewCreated: ${args.processId}" )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}