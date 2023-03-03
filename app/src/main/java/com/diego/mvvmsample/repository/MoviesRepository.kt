package com.diego.mvvmsample.repository

import androidx.paging.PagingData
import com.diego.mvvmsample.data.model.Movie
import com.diego.mvvmsample.data.model.MovieDetail
import com.diego.mvvmsample.db.MovieDatabase
import com.diego.mvvmsample.network.ApiResult
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getMovies(): Flow<PagingData<MovieDatabase>>

    suspend fun getMovieById(movieId: Int): ApiResult<MovieDetail>

    suspend fun insertMovieDetail(movieDetail: MovieDetail)

    suspend fun getMovieDetailById(movieDetailId: Int): MovieDetail?
}