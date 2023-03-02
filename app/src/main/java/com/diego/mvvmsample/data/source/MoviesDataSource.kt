package com.diego.mvvmsample.data.source

import androidx.paging.PagingData
import com.diego.mvvmsample.data.model.Movie
import com.diego.mvvmsample.data.model.MovieDetail
import com.diego.mvvmsample.network.ApiResult
import kotlinx.coroutines.flow.Flow

interface MoviesDataSource {

    fun getMovies(): Flow<PagingData<Movie>>

    suspend fun getMovieById(movieId: Int): ApiResult<MovieDetail>
}