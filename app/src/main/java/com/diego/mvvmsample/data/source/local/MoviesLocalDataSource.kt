package com.diego.mvvmsample.data.source.local

import androidx.paging.PagingData
import com.diego.mvvmsample.data.model.MovieDetail
import com.diego.mvvmsample.data.source.MoviesDataSource
import com.diego.mvvmsample.db.MovieDatabase
import com.diego.mvvmsample.db.MovieDetailDao
import com.diego.mvvmsample.network.ApiResult
import kotlinx.coroutines.flow.Flow

class MoviesLocalDataSource internal constructor(
    private val movieDetailDao: MovieDetailDao
) : MoviesDataSource {

    override fun getMovies(): Flow<PagingData<MovieDatabase>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieById(movieId: Int): ApiResult<MovieDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun insertMovieDetail(movieDetail: MovieDetail) {
        movieDetailDao.insert(movieDetail)
    }

    override suspend fun getMovieDetailById(movieDetailId: Int) =
        movieDetailDao.getMovieDetailById(movieDetailId)
}