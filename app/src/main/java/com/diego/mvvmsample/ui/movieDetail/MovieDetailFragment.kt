package com.diego.mvvmsample.ui.movieDetail

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.diego.mvvmsample.MyApplication
import com.diego.mvvmsample.R
import com.diego.mvvmsample.databinding.FragmentMovieDetailBinding
import com.diego.mvvmsample.utils.themeColor
import com.google.android.material.transition.MaterialContainerTransform

class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {

    private val args: MovieDetailFragmentArgs by navArgs()
    private val mMovieDetailViewModel: MovieDetailViewModel by viewModels {
        MovieDetailViewModelFactory(
            repository = (requireContext().applicationContext as MyApplication).moviesRepository,
            movieId = args.movieId
        )
    }

    private var _binding: FragmentMovieDetailBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(com.google.android.material.R.attr.colorSurface))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMovieDetailBinding.bind(view)
        _binding = binding
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            movieDetailViewModel = mMovieDetailViewModel
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}