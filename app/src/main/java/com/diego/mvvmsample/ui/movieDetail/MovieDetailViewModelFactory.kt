package com.diego.mvvmsample.ui.movieDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.diego.mvvmsample.repository.MoviesRepository

class MovieDetailViewModelFactory(
    private val repository: MoviesRepository,
    private val movieId: Int
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
            return MovieDetailViewModel(
                repository = repository,
                movieId = movieId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}