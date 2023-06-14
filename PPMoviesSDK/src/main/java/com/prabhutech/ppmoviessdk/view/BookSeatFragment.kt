package com.prabhutech.ppmoviessdk.view

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.prabhutech.ppmoviessdk.databinding.FragmentBookSeatBinding

private const val TAG = "BookSeatFragment"
class BookSeatFragment : Fragment() {
    private var _binding: FragmentBookSeatBinding? = null
    private val binding get() = _binding!!

    private val args: BookSeatFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookSeatBinding.inflate(inflater, container, false)

        Log.e(TAG, "onCreateView: ${args.movieId}" )
        Log.e(TAG, "onCreateView: ${args.processId}" )
        Log.e(TAG, "onCreateView: ${args.show}" )

        val progressDialog = ProgressDialog(requireContext()).apply {
            setTitle("Loading")
            setMessage("Please wait while we fetch movie details")
            setCancelable(false)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}