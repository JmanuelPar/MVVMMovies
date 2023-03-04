package com.diego.mvvmsample.ui.movies

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.diego.mvvmsample.MyApplication
import com.diego.mvvmsample.R
import com.diego.mvvmsample.adapter.MovieAdapter
import com.diego.mvvmsample.adapter.MovieListener
import com.diego.mvvmsample.adapter.MovieLoadStateAdapter
import com.diego.mvvmsample.data.model.Movie
import com.diego.mvvmsample.databinding.FragmentMoviesBinding
import com.google.android.material.transition.MaterialElevationScale
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class MoviesFragment : Fragment(R.layout.fragment_movies), MovieListener {

    private val mMoviesViewModel: MoviesViewModel by viewModels {
        MoviesViewModelFactory(
            (requireContext().applicationContext as MyApplication).moviesRepository
        )
    }

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var headerAdapter: MovieLoadStateAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMoviesBinding.bind(view)
        initView()
        showMovies()
        showUI()

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    override fun onMovieClicked(view: View, movie: Movie) {
        Timber.i("Movie : $movie")
        goToMovieDetailFragment(view, movie)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() {
        val mRvMovies = binding.rvMovies
        movieAdapter = MovieAdapter(this)
        headerAdapter = MovieLoadStateAdapter { movieAdapter.retry() }
        val footerAdapter = MovieLoadStateAdapter { movieAdapter.retry() }

        mRvMovies.apply {
            layoutManager = GridLayoutManager(mRvMovies.context, 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when {
                            position == 0 && headerAdapter.itemCount > 0 -> 2
                            position == movieAdapter.itemCount && footerAdapter.itemCount > 0 -> 2
                            else -> 1
                        }
                    }
                }
            }
            adapter = movieAdapter.withLoadStateHeaderAndFooter(
                header = headerAdapter,
                footer = footerAdapter
            )
            setHasFixedSize(true)
        }

        binding.buttonRetry.setOnClickListener {
            showLayoutError(false)
            movieAdapter.retry()
        }
    }

    private fun showMovies() {
        viewLifecycleOwner.lifecycleScope.launch {
            mMoviesViewModel.movies.collectLatest(movieAdapter::submitData)
        }
    }

    private fun showUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            movieAdapter.loadStateFlow.collect { loadState ->
                // Show a retry header if there was an error refreshing, and items were previously
                // cached OR default to the default prepend state
                headerAdapter.loadState = loadState.mediator
                    ?.refresh
                    ?.takeIf { it is LoadState.Error && movieAdapter.itemCount > 0 }
                    ?: loadState.prepend

                val isListEmpty =
                    loadState.source.refresh is LoadState.NotLoading && movieAdapter.itemCount == 0

                showProgressBar(loadState.mediator?.refresh is LoadState.Loading)
                showRecyclerView(
                    loadState.source.refresh is LoadState.NotLoading
                            || loadState.mediator?.refresh is LoadState.NotLoading
                )
                showLayoutNoResult(isListEmpty)

                val errorState = loadState.refresh as? LoadState.Error
                errorState?.let { loadStateError ->
                    val errorMessage = when (loadStateError.error) {
                        is IOException -> getString(R.string.no_connect_message)
                        is HttpException -> String.format(
                            getString(R.string.error_result_message_retry),
                            loadStateError.error.localizedMessage
                        )
                        else -> getString(R.string.error_result_message_unknown_retry)
                    }

                    if (movieAdapter.itemCount == 0) {
                        showError(errorMessage)
                    }
                }
            }
        }
    }

    private fun showProgressBar(visibility: Boolean) {
        binding.progressBar.isVisible = visibility
    }

    private fun showLayoutNoResult(visibility: Boolean) {
        binding.layoutNoResult.isVisible = visibility
    }

    private fun showRecyclerView(visibility: Boolean) {
        binding.rvMovies.isVisible = visibility
    }

    private fun showError(errorMessage: String) {
        showLayoutNoResult(false)
        showErrorMessage(errorMessage)
        showLayoutError(true)
    }

    private fun showErrorMessage(errorMessage: String) {
        binding.tvErrorMessage.text = errorMessage
    }

    private fun showLayoutError(visibility: Boolean) {
        binding.layoutError.isVisible = visibility
    }

    private fun goToMovieDetailFragment(view: View, movie: Movie) {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }

        val movieDetailCardTransitionName = getString(R.string.movie_detail_card_transition_name)
        view.findNavController().navigate(
            directions = MoviesFragmentDirections
                .actionMoviesFragmentToMovieDetailFragment(movieId = movie.idMovie),
            navigatorExtras = FragmentNavigatorExtras(view to movieDetailCardTransitionName)
        )
    }
}