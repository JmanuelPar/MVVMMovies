package com.diego.mvvmsample.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.diego.mvvmsample.repository.MoviesRepository

class MoviesViewModel(repository: MoviesRepository) : ViewModel() {

    val movies = repository.getMovies().cachedIn(viewModelScope)
}