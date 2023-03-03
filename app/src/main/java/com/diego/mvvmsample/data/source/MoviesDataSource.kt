package com.diego.mvvmsample.data.source

import androidx.paging.PagingData
import com.diego.mvvmsample.data.model.MovieDetail
import com.diego.mvvmsample.db.MovieDatabase
import com.diego.mvvmsample.network.ApiResult
import kotlinx.coroutines.flow.Flow

interface MoviesDataSource {

    fun getMovies(): Flow<PagingData<MovieDatabase>>

    suspend fun getMovieById(movieId: Int): ApiResult<MovieDetail>

    suspend fun insertMovieDetail(movieDetail: MovieDetail)

    suspend fun getMovieDetailById(movieDetailId: Int): MovieDetail?
}