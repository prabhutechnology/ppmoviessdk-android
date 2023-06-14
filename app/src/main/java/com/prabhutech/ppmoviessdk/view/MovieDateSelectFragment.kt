package com.prabhutech.ppmoviessdk.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.prabhutech.ppmoviessdk.databinding.FragmentMovieDateSelectBinding
import com.prabhutech.ppmoviessdk.model.model.responsebody.Theater
import com.prabhutech.ppmoviessdk.view.adapter.MovieDateAdapter

class MovieDateSelectFragment(
    private val theaterDates: List<Theater>,
    private val processId: String,
    private val movieId: String
) : Fragment() {
    private var _binding: FragmentMovieDateSelectBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMovieDateSelectBinding.inflate(inflater, container, false)
        binding.recyclerViewMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewMovies.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        val movieDateAdapter = MovieDateAdapter(requireContext(), this.theaterDates, processId, movieId)
        binding.recyclerViewMovies.adapter = movieDateAdapter
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}