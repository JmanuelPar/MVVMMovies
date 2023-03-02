package com.diego.mvvmsample.repository

import androidx.paging.PagingData
import com.diego.mvvmsample.data.model.Movie
import com.diego.mvvmsample.data.model.MovieDetail
import com.diego.mvvmsample.network.ApiResult
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getMovies(): Flow<PagingData<Movie>>

    suspend fun getMovieById(movieId: Int): ApiResult<MovieDetail>
}