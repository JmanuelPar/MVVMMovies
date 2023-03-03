package com.diego.mvvmsample.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.diego.mvvmsample.repository.MoviesRepository
import com.diego.mvvmsample.utils.transformAsMovie
import kotlinx.coroutines.flow.map

class MoviesViewModel(repository: MoviesRepository) : ViewModel() {

    val movies = repository.getMovies()
        .map { pagingData ->
            pagingData.transformAsMovie()
        }.cachedIn(viewModelScope)
}